package com.deliane.tcc.searcher;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.deliane.tcc.model.Biblioteca;
import com.deliane.tcc.model.CategoriaEnum;
import com.deliane.tcc.model.Documento;

public class Search {

	public List<Documento> textInfoSearchStopWords(String indexPath, String tipo, String termo)throws IOException, ParseException {
	   	 
	   	 List<Documento> documentos = new ArrayList();
	   	 CharArraySet stops = new CharArraySet(getStopWords(), true);
	   	 Analyzer analyzer = new StopAnalyzer(stops);
	   	 
	   	 TokenStream tokenStream = analyzer.tokenStream(LuceneConstants.CONTENTS, new StringReader(termo));
	   	 CharTermAttribute term = tokenStream.addAttribute(CharTermAttribute.class);
	   	 tokenStream.reset();
	   	 int j = 0;
	   	 String t= "";
	   	 while (tokenStream.incrementToken()) {
	        	j++;
	         	t+=  term.toString()+ " ";      	 
	    	}
	    	tokenStream.end();
	    	tokenStream.close();
	    	

	   	 Directory directory = (Directory) FSDirectory.open(Paths.get(indexPath));
	   	 DirectoryReader ireader = DirectoryReader.open(directory);//
	   	 IndexSearcher isearcher = new IndexSearcher(ireader);//

	    
	   	 QueryParser parser = new QueryParser(tipo, analyzer);
	   	 Query query = parser.parse(t);

	   	 ScoreDoc[] hits = isearcher.search(query, 100, Sort.INDEXORDER).scoreDocs;

	   	 for (int i = 0; i < hits.length; i++) {
	   		 Biblioteca biblioteca = new Biblioteca();
	   		 Documento documento = new Documento();
	   		 Document doc = isearcher.doc(hits[i].doc);

	   		 for (String info : doc.getValues("titulo")) {
	   			 documento.setTitulo(info);
	   			 System.out.println("titulo: " + info);
	   		 }

	   		 for (String info : doc.getValues("autor")) {
	   			 documento.setAutor(info);
	   			 System.out.println("autor: " + info);
	   		 }

	   		 for (String info : doc.getValues("assunto")) {
	   			 documento.setAssunto(info);
	   			 System.out.println("assunto: " + info);
	   		 }

	   		 for (String info : doc.getValues("biblioteca")) {
	   			 biblioteca.setNome(info);
	   			 System.out.println("biblioteca: " + info);
	   		 }

	   		 for (String info : doc.getValues("ano")) {
	   			 documento.setAno(Integer.parseInt(info));
	   			 System.out.println("ano: " + info);
	   		 }

	   		 for (String info : doc.getValues("instituicao")) {
	   			 biblioteca.setInstituicao(info);
	   			 System.out.println("instituicao: " + info);
	   		 }

	   		 for (String info : doc.getValues("endereco")) {
	   			 biblioteca.setEndereco(info);
	   			 System.out.println("endereco: " + info);
	   		 }
	   		 
	   		 for (String info : doc.getValues("lat")) {
	   			 biblioteca.setLat(Double.parseDouble(info));
	   			 System.out.println("lat: " + info);
	   		 }
	   		 
	   		 for (String info : doc.getValues("lng")) {
	   			 biblioteca.setLng(Double.parseDouble(info));
	   			 System.out.println("lng: " + info);
	   		 }

	   		 for (String info : doc.getValues("url")) {
	   			 biblioteca.setUrl(info);
	   			 System.out.println("url: " + info);
	   		 }
	   		 
	   		 for (String info : doc.getValues("categoria")) {
	   			 documento.setCategoria((info.equals("LIVRO"))? CategoriaEnum.LIVRO:CategoriaEnum.PRODUCAO_ACADEMICA);
	   			 System.out.println("categoria: " + info);
	   		 }
	   		 System.out.println("_______________________________________________________");
	   		 System.out.println("");
	   		 documento.setBiblioteca(biblioteca);
	   		 documentos.add(documento);    
	   	 }

	   	 ireader.close();
	   	 directory.close();
	   	 return documentos;
	    }

	    
	    
	    private Set<String> getStopWords(){
	   	 
	   	 Set<String> stopWords = new HashSet<String>();
	   	 stopWords.add("e");
	   	 stopWords.add("ou");
	   	 stopWords.add("em");
	   	 stopWords.add("de");
	   	 stopWords.add("da");
	   	 stopWords.add("do");
	   	 stopWords.add("na");
	   	 stopWords.add("no");
	   	 stopWords.add("a");
	   	 stopWords.add("o");
	   	 stopWords.add("um");
	   	 stopWords.add("uma");
	   	 stopWords.add("ante");
	   	 stopWords.add("ate");
	   	 stopWords.add("apos");
	   	 stopWords.add("com");
	   	 stopWords.add("contra");
	   	 stopWords.add("para");
	   	 stopWords.add("per");
	   	 stopWords.add("perante");
	   	 stopWords.add("por");
	   	 stopWords.add("sem");
	   	 stopWords.add("sob");
	   	 stopWords.add("sobre");
	   	 stopWords.add("trás");
	   	 
	   	 return stopWords;
	    }

}















