package spring.boot.oath2.websecurity.service;

public interface ConvertEntityToModel{

	public<F,D> D convertEntity(F clazF, D clazD);
	
}
