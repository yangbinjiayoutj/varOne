/**
 * 
 */
package com.haredb.sparkmonitor.node.reader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import com.haredb.sparkmonitor.node.MetricTuple;
import com.haredb.sparkmonitor.node.MetricsType;

/**
 * @author allen
 *
 */
public class FsMetricsReader extends MetricsReader {
	
	
	
	public FsMetricsReader(MetricsReader reader, MetricsType metricType){
		super(reader, metricType);
	}

	/* (non-Javadoc)
	 * @see com.haredb.sparkmonitor.node.reader.MetricsReader#read()
	 */
	@Override
	protected Map<String, List<MetricTuple>> read(String applicationId, String metricsDir) throws IOException {
		File root = new File(metricsDir);
		IOFileFilter filter = null;
		if(super.metricType.equals(MetricsType.FS)){
			filter = new RegexFileFilter(applicationId+"(.\\d+).executor.filesystem.*\\.csv");
		} else {
			filter = new RegexFileFilter(applicationId+"(.\\d+)." + super.metricType.type() + ".csv");
		}
		
		Collection<File> filteredFiles = FileUtils.listFiles(root, filter, null);
		
		Map<String, List<MetricTuple>> result = super.getMetricsData(filteredFiles);
		
		if(null != super.reader)
			result.putAll(super.reader.read(applicationId, metricsDir));
		
		return result;
	}

}