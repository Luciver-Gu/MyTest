package solrj.demo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

import entity.Article;

public class Test1 {
	
	/**
	 * 说明：创建索引（传统方式）
	 * @author Luciver_Gu
	 * @throws IOException 
	 * @throws SolrServerException 
	 * @time：2017年1月5日 下午2:48:05
	 */
	@Test
	public void createIndex1() throws SolrServerException, IOException {
//		使用HttpSolr服务端（HttpSolrServer）创建solr服务端对象
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost/solr/core1");
		// 使用solr输入文档（SolrInputDocument） 创建文档对象
		SolrInputDocument document = new SolrInputDocument();
		// 添加字段到文档对象
		document.addField("id", "3");
		document.addField("title", "这是来自solrj客户端的第一个title");
		document.addField("content", "这是来自solrj客户端的第一个content");
		//添加文档到solr服务器对象
		solrServer.add(document);
		solrServer.commit();
	}
	
	
	/**
	 * 说明：创建索引（bean注解方式)
	 * @author Luciver_Gu
	 * @throws SolrServerException 
	 * @throws IOException 
	 * @time：2017年1月5日 下午3:00:10
	 */
	@Test
	public void createIndex2() throws IOException, SolrServerException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		HttpSolrServer httpSolrServer = new HttpSolrServer("http://localhost/solr/core1");
		for (int i = 0; i < 30; i++) {
//			创建bean
			Article article = new Article();
			//添加值
			article.setId(""+i);
			article.setTitle("这是来自solrj客户端的第"+i+"个【title】 ------  "+i);
			article.setContent("这是来自solrj客户端的第"+i+"个【content】 ------  "+i);
			httpSolrServer.addBean(article);
		}
		httpSolrServer.commit();
	}
	
	
	
	
	
	/**
	 * 说明：根据id删除索引
	 * @author Luciver_Gu
	 * @throws IOException 
	 * @throws SolrServerException 
	 * @time：2017年1月5日 下午3:09:42
	 */
	@Test
	public void deleteIndex1() throws SolrServerException, IOException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost/solr/core1");
		solrServer.deleteById("0");
		solrServer.commit();
	}
	
	/**
	 * 说明：删除索引（根据Query表达式删除）
	 * @author Luciver_Gu
	 * @throws IOException 
	 * @throws SolrServerException 
	 * @time：2017年1月5日 下午3:13:09
	 */
	@Test
	public void deleteIndex2() throws SolrServerException, IOException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost/solr/core1");
		solrServer.deleteByQuery("id:1*");
		solrServer.commit();
	}
	
	
	/**
	 * 说明：查询索引（传统方式）
	 * @author Luciver_Gu
	 * @throws SolrServerException 
	 * @time：2017年1月5日 下午3:18:03
	 */
	@Test
	public void queryIndex1() throws SolrServerException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost/solr/core1");
		// 创建solr查询对象（solrquery）并且载入要查询的内容
		SolrQuery solrQuery = new SolrQuery("title:这是");
		// 添加返回结果的列
		solrQuery.addField("id");
		solrQuery.addField("title");
		// 设置查询结果返回的行数
		solrQuery.setRows(20);
		// 设置排序方式
		solrQuery.setSort("id", ORDER.desc);
		// 开始查询，返回查询响应对象（QueryResponse）
		QueryResponse response = solrServer.query(solrQuery);
		// 通过查询响应对象（QueryResponse）获得结果
		SolrDocumentList results = response.getResults();
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.getFieldValue("id"));
			System.out.println(solrDocument.getFieldValue("title"));
			System.out.println(solrDocument.getFieldValue("content"));
		}
	}
	
	
	/**
	 * 说明：查询结果（返回bean形式）
	 * @author Luciver_Gu
	 * @throws SolrServerException 
	 * @time：2017年1月5日 下午3:30:51
	 */
	@Test
	public void queryIndex2() throws SolrServerException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost/solr/core1");
		// 创建solr查询对象（solrquery）并且载入要查询的内容
		SolrQuery solrQuery = new SolrQuery("title:3 or id:5");
		// 设置返回结果的列
		solrQuery.addField("id");
		solrQuery.addField("title");
		// 设置查询结果返回的行数
		solrQuery.setRows(20);
		// 开始查询，返回查询响应对象（QueryResponse）
		QueryResponse response = solrServer.query(solrQuery);
		List<Article> articles = response.getBeans(Article.class);
		for (Article article : articles) {
			System.out.println(article.getId());
			System.out.println(article.getTitle());
			System.out.println(article.getContent());
		}
	}
	
	
	/**
	 * 说明：相似查询
	 * @throws SolrServerException
	 * @author Luciver_Gu
	 * @time：2017年1月5日 下午6:06:25
	 */
	@Test
	public void queryIndex3() throws SolrServerException{
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost/solr/core1");
		SolrQuery solrQuery = new SolrQuery("title:tgtla~");		
		solrQuery.addField("id");
		solrQuery.addField("title");
		solrQuery.setRows(20);
		QueryResponse response = solrServer.query(solrQuery);
		List<Article> articles = response.getBeans(Article.class);
		for (Article article : articles) {
			System.out.println(article.getId());
			System.out.println(article.getTitle());
			System.out.println(article.getContent());
		}
	}
	
	
	/**
	 * 说明：高亮查询
	 * @author Luciver_Gu
	 * @throws SolrServerException 
	 * @time：2017年1月5日 下午6:20:59
	 */
	@Test
	public void queryIndex4() throws SolrServerException {
		// 使用HttpSolr服务端（HttpSolrServer） 创建solr服务器端对象
		HttpSolrServer solrServer = new HttpSolrServer("http://localhost/solr/core1");
		// 创建solr查询对象（solrquery）并且载入要查询的内容
		// new SolrQuery("查询表达式")
		SolrQuery solrQuery = new SolrQuery("title:这是");
		// 设置要查询的列
		solrQuery.addField("id");
		solrQuery.addField("title");
		solrQuery.addField("content");
		solrQuery.setRows(20);
		/************************* 高亮设置及查询 ********************************/
		// 是否高亮
		solrQuery.setHighlight(true);
		// 摘要长度
		solrQuery.setHighlightFragsize(50);
		// 设置前后缀
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		// 添加高亮的field
		solrQuery.addHighlightField("title");
		// 开始查询，返回查询响应对象（QueryResponse）
		QueryResponse response = solrServer.query(solrQuery);
		
		System.out.println(response.getResponse());

		// 处理结果集  第一个Map的键是文档的ID，第二个Map的键是高亮显示的字段名
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		Set<String> keySet1 = highlighting.keySet();
		for (String key1 : keySet1) {
			Map<String, List<String>> map = highlighting.get(key1);
			Set<String> keySet2 = map.keySet();
			for (String key2 : keySet2) {
				List<String> list = map.get(key2);
				for (String string : list) {
					System.out.println(string);
				}
			}
		}
		
		
	}
	
	
	
	
	
	
}
