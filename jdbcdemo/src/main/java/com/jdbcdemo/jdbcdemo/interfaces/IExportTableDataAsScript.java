package com.jdbcdemo.jdbcdemo.interfaces;

import com.jdbcdemo.jdbcdemo.dto.BaseOutput;

public interface IExportTableDataAsScript {
	
	BaseOutput exportTableDataAsScript(String tableName);

}
