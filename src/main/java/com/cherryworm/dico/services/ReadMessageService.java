package com.cherryworm.dico.services;

import java.io.IOException;
import java.io.InputStream;

import com.cherryworm.dico.protos.SelfDescribingMessageProto.SelfDescribingMessage;

public interface ReadMessageService {
	
	public SelfDescribingMessage read(InputStream in) throws IOException;
	
}
