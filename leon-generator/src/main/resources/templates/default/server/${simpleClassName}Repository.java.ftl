package ${basepackage};

import org.springframework.stereotype.Repository;

import com.mawujun.repository.IRepository;

import ${basepackage}.${simpleClassName};
<#include "/java_copyright.include"/>

@Repository
public interface ${simpleClassName}Repository extends IRepository<${simpleClassName}, ${idType}>{


}
