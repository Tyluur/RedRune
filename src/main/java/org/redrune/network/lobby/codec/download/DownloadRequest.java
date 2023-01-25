package org.redrune.network.lobby.codec.download;

import io.netty.channel.ChannelHandlerContext;
import lombok.Getter;
import org.redrune.cache.CacheManager;

/**
 * @author Tyluur <itstyluur@gmail.com>
 * @since 7/19/2017
 */
public class DownloadRequest {
	
	/**
	 * The index id of the download request
	 */
	@Getter
	private final int indexId;
	
	/**
	 * The archive the request is in
	 */
	@Getter
	private final int archiveId;
	
	/**
	 * The priority of the request
	 */
	@Getter
	private final int priority;
	
	public DownloadRequest(int indexId, int archiveId, int priority) {
		this.indexId = indexId;
		this.archiveId = archiveId;
		this.priority = priority;
	}
	
	/**
	 * Sends the request to the channel
	 *
	 * @param ctx
	 * 		The channel handler context
	 */
	public void push(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(CacheManager.generateFile(indexId, archiveId, priority).getBuffer());
	}
	
	@Override
	public String toString() {
		return "DownloadRequest{" + "indexId=" + indexId + ", archiveId=" + archiveId + ", priority=" + priority + '}';
	}
}
