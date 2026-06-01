package com.bookshop.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bookshop.domain.Book;

//import com.bookshop.domain.Book;

public interface BookRepository extends JpaRepository<Book, Long>{

	Optional<Book> findById(Long id);
	List<Book> findByCategory(String category);
	
	List<Book> findByTitleContaining(String title);

}
