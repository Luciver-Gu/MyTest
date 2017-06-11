package entity;

import java.util.List;

import org.apache.solr.client.solrj.beans.Field;

/**
 * 说明：文章实体bean
 * @author Luciver_Gu
 * @version 1.0
 * @date 2017年1月5日
 */
public class Article {
	@Field
	private String id;
	@Field
	private String title;
	@Field
	private String content;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
}
