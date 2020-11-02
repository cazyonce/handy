package com.handy.sql.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.handy.sql.config.APIControlRequestMappingHandlerMapping;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

@Service
public class APIService {

//	@Autowired
//	private APIControlRequestMappingHandlerMapping apiControlMapping;

	@Autowired
	private RequestMappingHandlerMapping requestMappingHandlerMapping;
	
	public void addAPI(Map<String, String> map) throws Exception {
		if (!(requestMappingHandlerMapping instanceof APIControlRequestMappingHandlerMapping)) {
			throw new Exception("错误的请求处理映射对象");
		}
		APIControlRequestMappingHandlerMapping apiControlMapping = (APIControlRequestMappingHandlerMapping) requestMappingHandlerMapping;
		Object obj = createClass(map.get("path")).newInstance();
		Class classz =obj.getClass();
		apiControlMapping.registerMapping(classz.getMethod("test"), obj);
	}

	private Class createClass(String path) throws Exception {
		ClassPool pool = ClassPool.getDefault();

		CtClass cc = pool.makeClass("test.Person");

		ClassFile ccFile = cc.getClassFile();
		ConstPool constPool = ccFile.getConstPool();

		AnnotationsAttribute bodyAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation bodyAnnot = new Annotation(RequestMapping.class.getTypeName(), constPool);
		ArrayMemberValue arrayMemberValue = new ArrayMemberValue(constPool);
		arrayMemberValue.setValue(new StringMemberValue[] { new StringMemberValue(path, constPool) });
		bodyAnnot.addMemberValue("value", arrayMemberValue);
		bodyAttr.addAnnotation(bodyAnnot);
		ccFile.addAttribute(bodyAttr);

		bodyAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		bodyAnnot = new Annotation(RestController.class.getTypeName(), constPool);
		bodyAttr.addAnnotation(bodyAnnot);
		ccFile.addAttribute(bodyAttr);

		AnnotationsAttribute methodAttr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
		Annotation methodAnnot = new Annotation(GetMapping.class.getTypeName(), constPool);
		methodAttr.addAnnotation(methodAnnot);

		CtMethod ctMethod = new CtMethod(pool.get(String.class.getTypeName()), "test", new CtClass[] {}, cc);
		ctMethod.setModifiers(Modifier.PUBLIC);
		ctMethod.setBody("{System.out.println(\"getName\"); return \"This is test0.0...\";}");
		cc.addMethod(ctMethod);
		ctMethod.getMethodInfo().addAttribute(methodAttr);
		
		return cc.toClass();
	}
}
