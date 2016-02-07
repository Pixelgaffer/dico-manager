package com.cherryworm.dico.services;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.cherryworm.dico.events.DisconnectedEvent;
import com.cherryworm.dico.events.TaskResultReceivedEvent;
import com.cherryworm.dico.events.TaskStatusReceivedEvent;
import com.cherryworm.dico.protos.HandshakeProto.Handshake;
import com.cherryworm.dico.protos.SelfDescribingMessageProto.SelfDescribingMessage;
import com.cherryworm.dico.protos.SelfDescribingMessageProto.SelfDescribingMessage.MessageType;
import com.cherryworm.dico.protos.TaskResultProto.TaskResult;
import com.cherryworm.dico.protos.TaskStatusProto.TaskStatus;
import com.google.protobuf.Message;

@Service
public class SendToServerServiceImpl implements SendToServerService, Runnable {

	private Socket socket;
	@Autowired
	private ReadMessageService read;
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Override
	public void send(Message message, MessageType type) throws IOException {
		byte[] data = SelfDescribingMessage.newBuilder().setType(type).setData(message.toByteString()).build().toByteArray();
		socket.getOutputStream().write(ByteBuffer.allocate(4).putInt(data.length).order(ByteOrder.BIG_ENDIAN).array());
		socket.getOutputStream().write(data);
		socket.getOutputStream().flush();
	}

	@Override
	public void connect(String ip, int port) throws IOException {
		socket = new Socket(ip, port);
		new Thread(this).start();
		send(Handshake.newBuilder().setName("Manager").setManagesTasks(true).setRecievesResults(true).setRecievesStats(true).build(), MessageType.HANDSHAKE);
	}

	@Override
	public void run() {
		try {
			while (isConnected()) {
				SelfDescribingMessage message = read.read(socket.getInputStream());
				if(message == null) break;
				System.out.println("Received Message: " + message.getType() + " | " + Arrays.toString(message.getData().toByteArray()));
				
				switch (message.getType()) {
					case TASK_RESULT:
						publisher.publishEvent(new TaskResultReceivedEvent(this, TaskResult.parseFrom(message.getData())));
						break;
					case TASK_STATUS:
						publisher.publishEvent(new TaskStatusReceivedEvent(this, TaskStatus.parseFrom(message.getData())));
						break;
					default:
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		publisher.publishEvent(new DisconnectedEvent(this));
		socket = null;
	}

	@Override
	public boolean isConnected() {
		return socket != null;
	}

}
