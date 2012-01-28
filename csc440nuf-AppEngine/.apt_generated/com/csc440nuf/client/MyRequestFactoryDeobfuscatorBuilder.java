// Automatically Generated -- DO NOT EDIT
// com.csc440nuf.client.MyRequestFactory
package com.csc440nuf.client;
import java.util.Arrays;
import com.google.web.bindery.requestfactory.vm.impl.OperationData;
import com.google.web.bindery.requestfactory.vm.impl.OperationKey;
public final class MyRequestFactoryDeobfuscatorBuilder extends com.google.web.bindery.requestfactory.vm.impl.Deobfuscator.Builder {
{
withOperation(new OperationKey("8XNtMZJYJhx_HYy2I8x0DWydCMI="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/InstanceRequest;")
  .withDomainMethodDescriptor("()Ljava/lang/String;")
  .withMethodName("send")
  .withRequestContext("com.csc440nuf.client.MyRequestFactory$MessageRequest")
  .build());
withOperation(new OperationKey("ik3BotwkCiCjqtOEsWQP5aha6z8="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("()Ljava/lang/String;")
  .withMethodName("getMessage")
  .withRequestContext("com.csc440nuf.client.MyRequestFactory$HelloWorldRequest")
  .build());
withOperation(new OperationKey("rQabwC5q7bGYqrZ3n2VX_oAJLXQ="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/InstanceRequest;")
  .withDomainMethodDescriptor("()V")
  .withMethodName("register")
  .withRequestContext("com.csc440nuf.client.MyRequestFactory$RegistrationInfoRequest")
  .build());
withOperation(new OperationKey("_kvM9SBUHL0rkue6m4n_6A2xo$w="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/InstanceRequest;")
  .withDomainMethodDescriptor("()V")
  .withMethodName("unregister")
  .withRequestContext("com.csc440nuf.client.MyRequestFactory$RegistrationInfoRequest")
  .build());
withRawTypeToken("ZG14uTtNg2QZmQhccpOJsPZ8FUU=", "com.csc440nuf.shared.MessageProxy");
withRawTypeToken("TrJf9PfqYM68Ag66IlEC5ddFOD8=", "com.csc440nuf.shared.RegistrationInfoProxy");
withRawTypeToken("8KVVbwaaAtl6KgQNlOTsLCp9TIU=", "com.google.web.bindery.requestfactory.shared.ValueProxy");
withRawTypeToken("FXHD5YU0TiUl3uBaepdkYaowx9k=", "com.google.web.bindery.requestfactory.shared.BaseProxy");
withClientToDomainMappings("com.csc440nuf.server.Message", Arrays.asList("com.csc440nuf.shared.MessageProxy"));
withClientToDomainMappings("com.csc440nuf.server.RegistrationInfo", Arrays.asList("com.csc440nuf.shared.RegistrationInfoProxy"));
}}
