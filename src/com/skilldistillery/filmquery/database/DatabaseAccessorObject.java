package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) {
		String mySql = "select * from film where id = ?";
		Film film = null;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			pst = conn.prepareStatement(mySql);
			pst.setInt(1, filmId);
			rs = pst.executeQuery();

			if (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				String description = rs.getString(3);
				int releaseYear = rs.getInt(4);
				int languageId = rs.getInt(5);
				int rentalDuration = rs.getInt(6);
				double rentalRate = rs.getDouble(7);
				int length = rs.getInt(8);
				double replacementCost = rs.getDouble(9);
				String rating = rs.getString(10);
				String features = rs.getString(11);

				film = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length,
						replacementCost, rating, features);

				List<Actor> actors = findActorsByFilmId(id);
				film.setActors(actors);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pst, rs);
		}
		return film;
	}

	@Override
	public Actor findActorById(int actorId) {
		String mySql = "select * from actor where id = ?";
		Actor actor = null;

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			pst = conn.prepareStatement(mySql);
			pst.setInt(1, actorId);
			rs = pst.executeQuery();

			if (rs.next()) {
				int id = rs.getInt(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);

				actor = new Actor(id, firstName, lastName);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pst, rs);
		}
		return actor;
	}

	@Override
	public List<Actor> findActorsByFilmId(int filmId) {
		String mySql = "select a.id, a.first_name, a.last_name, f.id from actor a "
				+ "join film_actor fa on a.id = fa.actor_id " + "join film f on fa.film_id = f.id " + "where f.id =? ";

		List<Actor> actors = new ArrayList<>();

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			pst = conn.prepareStatement(mySql);
			pst.setInt(1, filmId);
			rs = pst.executeQuery();

			while (rs.next()) {
				int id = rs.getInt(1);
				String firstName = rs.getString(2);
				String lastName = rs.getString(3);

				Actor actor = new Actor(id, firstName, lastName);
				actors.add(actor);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pst, rs);
		}
		return actors;
	}
	
	@Override
	public List<Film> findFilmsByKeyword(String keyword){
		List<Film> films = new ArrayList<>();
		String mySql = "select * from film "
				+ "where title like ? or description like ?";
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			pst = conn.prepareStatement(mySql);
			pst.setString(1, "%"+keyword+"%");
			pst.setString(2, "%"+keyword+"%");
			rs = pst.executeQuery();

			while (rs.next()) {
				int id = rs.getInt(1);
				String title = rs.getString(2);
				String description = rs.getString(3);
				int releaseYear = rs.getInt(4);
				int languageId = rs.getInt(5);
				int rentalDuration = rs.getInt(6);
				double rentalRate = rs.getDouble(7);
				int length = rs.getInt(8);
				double replacementCost = rs.getDouble(9);
				String rating = rs.getString(10);
				String features = rs.getString(11);

				Film film = new Film(id, title, description, releaseYear, languageId, rentalDuration, rentalRate, length,
						replacementCost, rating, features);
				List<Actor> actors = findActorsByFilmId(id);
				film.setActors(actors);
				films.add(film);

			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pst, rs);
		}
		return films;
	}
	
	@Override
	public String findFilmLanguage(int filmId) {
		String mySql = "select l.name from film f join language l on f.language_id = l.id "
				+ "where f.id = ?";
		String languageName = null;
		languageName = getString(mySql, filmId);
		return languageName;
	}

	@Override
	public String findFilmCategory(int filmId) {
		String mySql = "select c.name from film f "
				+ "join film_category fc on f.id = fc.film_id "
				+ "join category c on fc.category_id = c.id "
				+ "where f.id = ?";
		String category = null;
		category = getString(mySql, filmId);
		return category;
	}
	
	private String getString(String mySql, int filmId) {
		String str = null;
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			pst = conn.prepareStatement(mySql);
			pst.setInt(1, filmId);
			rs = pst.executeQuery();

			if (rs.next()) {
				str = rs.getNString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pst, rs);
		}
		return str;
	}
	
	@Override
	public List<String> findCopiesInventory(int filmId){
		String mySql = "select i.media_condition from film f "
				+ "join inventory_item i on f.id = i.film_id "
				+ "where f.id = ?;";
		List<String> conditions = new ArrayList<>();
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DriverManager.getConnection(URL, user, pass);
			pst = conn.prepareStatement(mySql);
			pst.setInt(1, filmId);
			rs = pst.executeQuery();

			while (rs.next()) {
				String condition = rs.getString(1);
				conditions.add(condition);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			closeResource(conn, pst, rs);
		}
		return conditions;
	}
	
	private void closeResource(Connection conn, PreparedStatement pst, ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pst != null) {
				pst.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
