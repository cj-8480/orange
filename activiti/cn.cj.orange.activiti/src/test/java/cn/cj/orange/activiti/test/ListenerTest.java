package cn.cj.orange.activiti.test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class ListenerTest {

	@Autowired
	private ProcessEngine processEngine;

	@Test
	public void start() {
		RepositoryService repositoryService = processEngine.getRepositoryService();
/*
		List<Deployment> deplist2 = repositoryService.createDeploymentQuery().list();
		for (Deployment deploy : deplist2) {
			System.out.println(deploy.getName() + "\t" + deploy.getDeploymentTime());
			repositoryService.deleteDeployment(deploy.getId());
		}

		List<Model> list = repositoryService.createModelQuery().list();
		for (Model model : list) {
			System.out.println(model.getKey());
			String processName = model.getName() + ".bpmn20.xml";
			Deployment deploy = repositoryService.createDeployment().name(model.getName())
					.addInputStream(processName, getStreamByModel(repositoryService, model.getId())).deploy();
//					.addBpmnModel(model.getName(), getStreamByModel(repositoryService, model.getId())).deploy();
			System.out.println(deploy.getName());
		}

		List<Deployment> list2 = repositoryService.createDeploymentQuery().list();
		for (Deployment deploy : list2) {
			System.out.println(deploy.getName() + "\t" + deploy.getDeploymentTime());
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
					.deploymentId(deploy.getId()).singleResult();
			System.out.println(processDefinition.getId() + "\t" + processDefinition.getKey());
		}*/

		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance instance = runtimeService.startProcessInstanceByKey("process");
		String name = instance.getName();
		System.out.println(name);
	}

	private ByteArrayInputStream getStreamByModel(RepositoryService repositoryService, String modelId) {
		byte[] bpmnBytes = null;
		JsonNode editorNode = null;
		try {
			editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(modelId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
		BpmnModel model = jsonConverter.convertToBpmnModel(editorNode);
		bpmnBytes = new BpmnXMLConverter().convertToXML(model);
		return new ByteArrayInputStream(bpmnBytes);
	}
}
