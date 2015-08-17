package kr.co.koreanmagic.commons.api;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.apache.log4j.Logger;

public class ByteBufferPool {
	
	private Logger logger = Logger.getLogger(getClass());
	
	private static final int MEMORY_BLOCKSIZE = 8912*10; // Buffer 개당 사이즈
	private static final int DEFAULT_BUFFER_SIZE = MEMORY_BLOCKSIZE * 20;
	
	private final Queue<ByteBuffer> queue = new LinkedList<>();
	private boolean isWait = false; // 버퍼가 비었을떄 기다리게 할건지 결정하는 플래그
	
	public ByteBufferPool() {
		this(DEFAULT_BUFFER_SIZE, false);
	}
	public ByteBufferPool(int memorySize) {
		this(memorySize, false);
	}
	public ByteBufferPool(int memorySize, boolean wait) {
		initMemoryBuffer(memorySize > 0 ? memorySize : DEFAULT_BUFFER_SIZE);
		setWait(wait);
	}
	private void initMemoryBuffer(int size) {
		int bufferCount = size / MEMORY_BLOCKSIZE;
		size = bufferCount * MEMORY_BLOCKSIZE;
		ByteBuffer directBuf = ByteBuffer.allocateDirect(size);
		divideBuffer(directBuf, MEMORY_BLOCKSIZE, queue);
	}

	// 바이트 버퍼 만들기
	private void divideBuffer(ByteBuffer buf, int block,
			Queue<ByteBuffer> queue) {
		int bufferCount = buf.capacity() / block; // 블럭이 몇 개 나오는지 계산한다.
		int position = 0;
		int max =-1;
		for(int i = 0; i < bufferCount; i++) { // 블럭수 만큼 루프를 돌린다.
			max = position + block;
			buf.limit(max); // 리미트로 뒷부분 결정
			queue.offer(buf.slice()); // 잘라낸다.
			position = max;
			buf.position(position);
		}
		
	}
	
	public void putBuffer(ByteBuffer buf) {
		if(buf.isDirect()) {
			buf.clear(); // 초기화 시켜준다.
			synchronized(queue) {
				queue.add(buf);
				queue.notifyAll();
			}
		}
	}
	
	public ByteBuffer getBuffer() {
		ByteBuffer buf = null;
		synchronized(queue) {
			
			if(queue.isEmpty()) {
				if(isWait()) { // 기다리는 모드이면
					try {
						queue.wait();
						/*
						 * 인터럽트가 나는 상황은 어떤게 있을까?
						 * 아마도 정상적인 루틴이 이루어지기 힘든 상황일 것이다. 따라서 null을 내보낸다.
						 */
					} catch (InterruptedException e) {
						buf = null;
					}
				}
				else {
					buf = getBuffer(MEMORY_BLOCKSIZE);
				}
			} else {
				buf = queue.remove();
			}
		}
		return buf;
	}
	// 비었을때는 만든다
	private ByteBuffer getBuffer(int block) {
		logger.debug("ByteBufferPool is Empty!!");
		return ByteBuffer.allocate(block);
	}
	
	// 버퍼가 비었을때 기다리게 할건지.
	public synchronized void setWait(boolean wait) { this.isWait = wait; }	
	public synchronized boolean isWait() { return isWait; }

}
