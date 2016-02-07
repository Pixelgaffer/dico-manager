package com.cherryworm.dico.services;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.cherryworm.dico.protos.SelfDescribingMessageProto.SelfDescribingMessage;

@Service
public class ReadMessageServiceImpl implements ReadMessageService {

	@Override
	public SelfDescribingMessage read(InputStream in) throws IOException {
		BufferedInputStream bin = new BufferedInputStream(in);
		byte[] lengthArray = new byte[4];
		
		if(bin.read(lengthArray) == -1)
			return null;
		
		int length = Math.abs(ByteBuffer.wrap(lengthArray).order(ByteOrder.BIG_ENDIAN).getInt());
		
		byte[] buffer = new byte[length];
		
		if(bin.read(buffer) == -1)
			return null;
		
		return SelfDescribingMessage.parseFrom(buffer);
	}

}
