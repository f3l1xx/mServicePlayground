package de.fb.hystrix;

import com.netflix.hystrix.HystrixCommandGroupKey;

public class CmdGroup {
	public static HystrixCommandGroupKey DEFAULT = HystrixCommandGroupKey.Factory.asKey("Default");
	public static HystrixCommandGroupKey SLOW = HystrixCommandGroupKey.Factory.asKey("slow");
	public static HystrixCommandGroupKey LEGACY = HystrixCommandGroupKey.Factory.asKey("legacy");
}
