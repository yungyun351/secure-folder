package com.marttapps.securefolder.model.listener;

import java.nio.file.Path;

/**
 * 檔案處理進度監聽器
 */
public interface EncFileProgressListener {

	/**
	 * 流程開始處理前呼叫
	 * 
	 * @param totalFiles 檔案總數
	 */
	void onStart(int totalFiles);

	/**
	 * 檔案開始處理前呼叫
	 * 
	 * @param index 當前index
	 * @param file  當前檔案
	 */
	void onFileStart(int index, Path file);

	/**
	 * 檔案開始處理完成後呼叫
	 * 
	 * @param index     當前index
	 * @param file      當前檔案
	 * @param success   是否成功
	 * @param exception 失敗的例外
	 */
	void onFileDone(int index, Path file, boolean success, Exception exception);

	/**
	 * 是否要求終止流程
	 * 
	 * @return 是否要求終止流程
	 */
	boolean isCancelled();

	/**
	 * 執行完成後呼叫
	 */
	void onCompleted();
}
