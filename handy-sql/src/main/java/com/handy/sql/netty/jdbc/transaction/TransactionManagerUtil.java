package com.handy.sql.netty.jdbc.transaction;

import org.springframework.transaction.TransactionStatus;
import com.handy.sql.netty.GlobalProvide;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TransactionManagerUtil implements AutoCloseable {
	private TransactionStatus status;

	public static TransactionManagerUtil open() {
		return new TransactionManagerUtil(GlobalProvide.TRANSACTION_MANAGER.getTransaction(null));
	}

	public void commit() {
		GlobalProvide.TRANSACTION_MANAGER.commit(status);
		status = null;
	}

	@Override
	public void close() throws Exception {
		if (status != null) {
			GlobalProvide.TRANSACTION_MANAGER.rollback(status);
		}
	}

}
