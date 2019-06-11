package cn.cj.orange.listener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class myListener implements ExecutionListener{

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println(execution.getEventName());
	}

}
