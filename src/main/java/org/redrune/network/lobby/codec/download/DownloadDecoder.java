package org.redrune.network.lobby.codec.download;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.redrune.cache.CacheFileStore;
import org.redrune.core.EngineWorkingSet;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 5/18/2017
 */
public class DownloadDecoder extends ByteToMessageDecoder {
	
	/**
	 * The download requests
	 */
	private LinkedList<DownloadRequest> requests = new LinkedList<>();
	
	/**
	 * If the checksum was sent [255, 255]
	 */
	private AtomicBoolean checksumSent = new AtomicBoolean(false);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while(in.readableBytes() >= 4) {
			serveRequest(ctx, in, in.readByte() & 0xFF);
		}
		pushAwaiting(ctx, true);
	}
	
	/**
	 * Serves the request
	 *
	 * @param ctx
	 * 		The channel context
	 * @param in
	 * 		The buffer
	 * @param priority
	 * 		The priority of the request
	 */
	private void serveRequest(ChannelHandlerContext ctx, ByteBuf in, int priority) {
		final int indexId = in.readByte() & 0xFF;
		final int archiveId = in.readShort() & 0xFFFF;
		if (indexId != 255) {
			if (CacheFileStore.STORE.getIndexes().length <= indexId || CacheFileStore.STORE.getIndexes()[indexId] == null || !CacheFileStore.STORE.getIndexes()[indexId].archiveExists(archiveId)) {
				System.out.println("Index did not exist: " + indexId);
				return;
			}
		} else if (archiveId != 255) {
			if (CacheFileStore.STORE.getIndexes().length <= archiveId || CacheFileStore.STORE.getIndexes()[archiveId] == null) {
				System.out.println("Archive did not exist: " + archiveId);
				return;
			}
		}
		final DownloadRequest downloadRequest = new DownloadRequest(indexId, archiveId, priority);
		switch (priority) {
			case 0:
				requests.addLast(downloadRequest);
				break;
			case 1:
				requests.addFirst(downloadRequest);
				break;
			default:
				break;
		}
		if (checksumSent.get()) {
			pushAwaiting(ctx, false);
		}
	}
	
	private void pushAwaiting(ChannelHandlerContext ctx, boolean setVerified) {
		if (setVerified) {
			this.checksumSent.set(true);
		}
		DownloadRequest request;
		while ((request = requests.poll()) != null) {
			final DownloadRequest finalRequest = request;
			EngineWorkingSet.submitJs5Work(() -> {
				try {
					finalRequest.push(ctx);
					checksumSent.compareAndSet(false, true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}
	}
	
}