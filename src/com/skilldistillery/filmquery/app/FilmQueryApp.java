package com.skilldistillery.filmquery.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	private DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}


	private void launch() {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) {
		int choice = -1;
		while(choice != 3) {
			showMenu();
			System.out.print("Please enter your choice(1 - 3): ");
			choice = input.nextInt();
			input.nextLine();
			
			switch(choice) {
			case 1: 
				System.out.print("Please enter film id: ");
				int filmId = input.nextInt();
				input.nextLine();
				findFilmById(filmId, input);
				break;
			case 2:
				System.out.print("Please enter keyword of film title or description: ");
				String keyword = input.nextLine();
				findFilmsByKeyword(keyword, input);
				break;
			case 3:
				System.out.println("Thank you for visiting, goodbye!");
				break;
			default:
				System.out.println("The input is invalid, please enter again!");
				break;
			}
		}
	}
	
	private void showMenu() {
		System.out.println();
		System.out.println(" --------------- main-menu -------------------");
		System.out.println("|   1.Look up a film by its id                |");
		System.out.println("|   2.Look up a film by a search keyword      |");
		System.out.println("|   3.Exit the application                    |");
		System.out.println(" --------------------------------------------");
		System.out.println();
	}
	
	private void findFilmById(int filmId, Scanner input) {
		Film film = db.findFilmById(filmId);
		List<Film> films = new ArrayList<>();
		if(film != null) {
			films.add(film);
		}
		
		displayFilm(films);
		if(films.size() > 0) {
			subMenu(films, input);
		}
	}
	
	private void findFilmsByKeyword(String keyword, Scanner input){
		List<Film> films = db.findFilmsByKeyword(keyword);

		displayFilm(films);
		
		if(films.size() > 0) {
			subMenu(films, input);
		}
	}
	
	private void displayFilm(List<Film> films) {
		if(films.size() == 0) {
			System.out.println("The film is not found!");
			return;
		}
		System.out.println("************** display films **************");
		System.out.println();
		for(Film film : films) {
			String filmLanguage = db.findFilmLanguage(film.getId());
			List<Actor> actors = film.getActors();
			System.out.println("title:"+film.getTitle()+" year:"+film.getReleaseYear()+
					" rating:"+film.getRating()+" description:"+film.getDescription()+" film language:"+filmLanguage);
			System.out.println("actors: "+actors);
		}
	}
	
	private void subMenu(List<Film> films, Scanner input) {
		while(true) {
			System.out.println("    -------------- sub-menu ----------------");
			System.out.println("   |   1.Return to the main menu            |");
			System.out.println("   |   2.View all film details              |");
			System.out.println("    ----------------------------------------");
			System.out.print("Please enter your choice(1 - 2): ");
			int select = input.nextInt();
			if(select == 1) {
				break;
			}else if(select == 2) {
				System.out.println("************** display film details **************");
				System.out.println();
				filmDetails(films);
				break;
			}else {
				System.out.println("Invalid input, please select again: ");
			}
		}
	}

	private void filmDetails(List<Film> films) {
		for (Film film : films) {
			System.out.println(film);
			String category = db.findFilmCategory(film.getId());
			System.out.println("category:"+category);
			List<String> conditions =  db.findCopiesInventory(film.getId());
			System.out.println("film id:" + film.getId() + " copies: " + conditions.size()+" in inventory");
			System.out.println("conditions: " + conditions);
		}
	}
	
	private void test() {
		Film film = db.findFilmById(1);
		System.out.println(film);
	}
}
