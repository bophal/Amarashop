package com.bookshop.service;

import com.bookshop.domain.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
	List<com.bookshop.domain.Book> findAll ();

	Optional<Book> findOne(Long id);

	List<Book> findByCategory(String category);	
	
	List<Book> blurrySearch(String title);
	
	Optional<Book> findById(Long id);

}
