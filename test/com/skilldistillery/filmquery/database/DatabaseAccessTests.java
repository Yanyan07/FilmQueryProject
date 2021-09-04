package com.skilldistillery.filmquery.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

class DatabaseAccessTests {
	private DatabaseAccessor db;

	@BeforeEach
	void setUp() throws Exception {
		db = new DatabaseAccessorObject();
	}

	@AfterEach
	void tearDown() throws Exception {
		db = null;
	}

	@Test
	void test_getFilmById_with_invalid_id_returns_null() {
		Film f = db.findFilmById(-42);
		assertNull(f);
	}

	@Test
	void test_getFilmById_with_valid_id_returns_value() {
		int filmId = 1;
		Film output = db.findFilmById(filmId);
		Film expected = new Film();
		expected.setId(1);
		expected.setTitle("ACADEMY DINOSAUR");
		expected.setDescription("A Epic Drama of a Feminist And a Mad Scientist who must Battle a Teacher in The Canadian Rockies");
		expected.setReleaseYear(1993);
		expected.setLanguageId(3);
		expected.setRentalDuration(6);
		expected.setRentalRate(0.99);
		expected.setLength(86);
		expected.setReplacementCost(20.99);
		expected.setRating("PG");
		expected.setFeatures("Deleted Scenes,Behind the Scenes");
		
		assertEquals(expected, output);
	}
	
	@Test
	void test_findActorById_with_invalid_id_returns_null() {
		Actor a = db.findActorById(-42);
		assertNull(a);
	}
	
	@Test
	void test_findActorById_with_valid_id_returns_value() {
		Actor output = db.findActorById(10);
		Actor expected = new Actor(10, "Christian", "Gable");
		assertEquals(expected, output);
	}
	
	@Test
	void test_findActorsByFilmId_with_invalid_id_returns_empty() {
		List<Actor> actors = db.findActorsByFilmId(-42);
		assertEquals(0, actors.size());
	}
	
	
}
