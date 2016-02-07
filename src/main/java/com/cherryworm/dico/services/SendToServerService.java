package com.cherryworm.dico.services;

import java.io.IOException;

import com.cherryworm.dico.protos.SelfDescribingMessageProto.SelfDescribingMessage.MessageType;
import com.google.protobuf.Message;

public interface SendToServerService {

	public void send(Message message, MessageType type) throws IOException;
	public void connect(String ip, int port) throws IOException;
	public boolean isConnected();

}
