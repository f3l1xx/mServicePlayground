package de.fb.hystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

public class CacheTestHyCommand extends HystrixCommand<String> {

	private String cacheKey;
	private String name;

	public CacheTestHyCommand(String cacheKey,String name, HystrixCommandGroupKey group) {
		super(group);
		this.cacheKey = cacheKey;
		this.name = name;
	}

	@Override
	protected String run() throws Exception {
		return name + Math.random();
	}

	@Override
	protected String getCacheKey() {
		return cacheKey;
	}

}
