package org.obj2openjl.v3.matcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.obj2openjl.v3.io.LineReader;

public class MatchHandlerPool {
	
	private List<MatchHandler<?>> matchHandlers = new ArrayList<MatchHandler<?>>();
	
	public MatchHandlerPool(Collection<? extends MatchHandler<?>> matchHandlers) {
		this.addMatchHandlers(matchHandlers);
	}
	
	public MatchHandlerPool(MatchHandler<?>... matchHandlers) {
		this.addMatchHandlers(matchHandlers);
	}
	
	public void addMatchHandler(MatchHandler<?> matchHandler) {
		if(matchHandler != null) {
			this.matchHandlers.add(matchHandler);
		}
	}
	
	public void addMatchHandlers(Collection<? extends MatchHandler<?>> matchHandlers) {
		Iterator<? extends MatchHandler<?>> handlerIterator = matchHandlers.iterator();
		
		while(handlerIterator.hasNext()) {
			this.addMatchHandler(handlerIterator.next());
		}
	}
	
	public void addMatchHandlers(MatchHandler<?>... matchHandlers) {
		for(MatchHandler<?> matchHandler: matchHandlers) {
			this.addMatchHandler(matchHandler);
		}
	}
	
	public void removeMatchHandler(MatchHandler<?> matchHandler) {
		this.matchHandlers.remove(matchHandler);
	}
	
	public void handleLine(String line) {
		Iterator<MatchHandler<?>> handlerIterator = this.matchHandlers.iterator();
		
		while(handlerIterator.hasNext()) {
			handlerIterator.next().matchString(line);
		}
	}
	
	public void handleAll(LineReader lineReader) {
		String line;
		while((line = lineReader.readLine()) != null) {
			this.handleLine(line);
		}
	}

}
