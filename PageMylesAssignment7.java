import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.PriorityQueue;
import java.util.Scanner;

/*
Myles Page
Cs 1450 002
Monday - Wednesday
Due 03-31-2021
Assignment 7
Priority Queue
*/

public class PageMylesAssignment7 {
	
	public static void main(String[] args) throws FileNotFoundException{
		
		//reads in the files
		File read = new File("Venue.txt");
		Scanner venueRead = new Scanner(read);
		File athletes = new File("athletes.txt");
		Scanner athleteRead = new Scanner(athletes);
		
		
		//reads in the venue and makes the object 
		String venueName = venueRead.nextLine();
		int readyRoom = venueRead.nextInt();
		int pool = venueRead.nextInt();
		
		//makes a venue object
		AquaticsCenter venue = new AquaticsCenter(venueName, readyRoom, pool);
		
		
		
		//reads the athlete function
		while(athleteRead.hasNext()) {
			int seat = athleteRead.nextInt();
			String team = athleteRead.next();
			double time = athleteRead.nextDouble();
			String event = athleteRead.next();
			String name = athleteRead.nextLine();
			Athlete athlete = new Athlete(team, time, event, name);
			venue.addAthleteToReadyRoom(seat, athlete);
		}
		
		//closes the scanners
		venueRead.close();
		athleteRead.close();
		
		//makes the race and referee objects
		Race race = new Race();
		RaceReferee raceReferee = new RaceReferee();
		
		//calls functions
		raceReferee.moveAthletesToWaitingForIntro(venue, race);
		raceReferee.moveAthletesToPoolLanes(venue, race);
		venue.printPool();
		raceReferee.startRace(venue, race);
		raceReferee.displayRaceResults(race);
	}
}

class AquaticsCenter {
		
	//variables
	private String name;				
	private int numberReadyRoomSeats;	
	private Athlete[] readyRoom;								
	private int numberLanes;			
	private Athlete[] pool;			
											
	//constructor
	public AquaticsCenter (String name, int numberReadyRoomSeats, int numberLanes) {
		this.name = name;
		this.numberReadyRoomSeats = numberReadyRoomSeats;
		this.numberLanes = numberLanes;	

		readyRoom = new Athlete[numberReadyRoomSeats];
		pool = new Athlete[numberLanes];
	}
		
	//getters
	public String getName() {
		return name;
	}
	public int getNumberReadyRoomSeats() {
		return numberReadyRoomSeats;
	}
	public int getNumberLanes() {
		return numberLanes;
	}
	
	//Adders
	public void addAthleteToReadyRoom (int seat, Athlete athlete) {
		readyRoom[seat] = athlete; 
	}
	public void addAthleteToPool (int lane, Athlete athlete) {
		pool[lane] = athlete; 
	}

	//getters
	public Athlete getAthleteInReadyRoom(int seat) {
		return readyRoom[seat];
	}
	public Athlete getAthleteInPool(int lane) {
		return pool[lane];
	}

	//removes
	public Athlete removeAthleteFromReadyRoom (int seat) {
		return readyRoom[seat];
	}
	public Athlete removeAthleteFromPool (int lane) {
		return pool[lane];
	}	
	
	//prints the pool
	public void printPool() {
		int place = 1;
		System.out.println("\n\nPool\nName \t\t\tTime \t    Event \t\tTeam");
		while(place < numberLanes) {
			Athlete placeholder = getAthleteInPool(place);
			System.out.println(placeholder);
			place++;
		}
	}
}
	
	
	
class Athlete implements Comparable<Athlete> {
		
	//varables
	private String team;			
	private double time;			
	private String event;			
	private String name;			

	// Create an athlete
	public Athlete (String team, double time, String event, String name) {
		this.team = team;
		this.time = time;
		this.event = event;
		this.name = name;		
	}
		
	//getters
	public String getTeam() {
		return team;
	}
	public double getTime() {
		return time;
	}
	public String getEvent() {
		return event;
	}
	public String getName() {
		return name;
	}

	//Updates the time
	public void updateTime(double secondsToAdd) {
		time = time + secondsToAdd;
	}

	//formated print
	public String toString() {
		return String.format("%s     \t%.2f\t    %-10s  \t%-10s", name,time,event,team);
	}
	
	//comparable
	public int compareTo(Athlete otherAthlete) {
		if (this.time < otherAthlete.time) {
			return -1;
		}
		else if (this.time > otherAthlete.time) {
			return 1;
		}
		else {
			return 0;
		}
	}
}
	
class Race{
	
	//variables
	PriorityQueue<Athlete> waitingForIntro = new PriorityQueue<Athlete>();
	PriorityQueue<Athlete> resultsQueue = new PriorityQueue<Athlete>();
	
	//constructor
	public Race() {
		waitingForIntro = new PriorityQueue<Athlete>();
		resultsQueue = new PriorityQueue<Athlete>();
	}
	//is empty
	public boolean isWaitingForIntroEmpty() {
		return waitingForIntro.isEmpty();
	}
	public boolean isResultsQueueEmpty() {
		return resultsQueue.isEmpty();
	}

	//Adders
	public void addAthleteToWaitingForIntro(Athlete athlete) {
		waitingForIntro.add(athlete);
	}
	public void addResultsQueue(Athlete athlete) {
		resultsQueue.add(athlete);
	}
	
	//Removes
	public Athlete removeAthleteToWaitingForIntro() {
		return waitingForIntro.remove();
	}
	public Athlete removeResultsQueue() {
		return resultsQueue.remove();
	}
}
		
class RaceReferee{
	
	//moves Athlete
	public void moveAthletesToWaitingForIntro (AquaticsCenter aquaticsCenter, Race race) {
		//checks each spot
		for(int i = 0; i < aquaticsCenter.getNumberReadyRoomSeats(); i++) {
			//removes the athlete
			Athlete placeholder = aquaticsCenter.removeAthleteFromReadyRoom(i);
			//check if spot empty or not 
			if(placeholder != null) {
				System.out.println("Seat " + i + " moved to waiting for introduction: " + placeholder.getName());
				race.addAthleteToWaitingForIntro(placeholder);
				
			}
		}
	}
	
	//moves to pool 
	public void moveAthletesToPoolLanes (AquaticsCenter aquaticsCenter, Race race) {
		//varribles
		int[] seat = {4,5,3,6,2,7,1,8};
		int place = 0;
		System.out.println("\n\n");
		//while queue is not empty 
		while(race.isWaitingForIntroEmpty() == false) {
			//removes the athlete
			Athlete placeholder = race.removeAthleteToWaitingForIntro();
			//prints and moves 
			System.out.println("Please welcome in lane # " + seat[place] + ":" + placeholder.getName());
			aquaticsCenter.addAthleteToPool(seat[place], placeholder);
			place++;
		}
	}
	
	public void startRace (AquaticsCenter aquaticsCenter, Race race) {
		System.out.println("\n\nReady Set Go\nName \t\t   Qualifying \tAdded \tTotal");
		for(int i = 0; i < aquaticsCenter.getNumberLanes(); i++) {
			
			Athlete placeholder = aquaticsCenter.removeAthleteFromPool(i);
		
			if(placeholder != null) {
				double addTime = Math.random();
				double time = placeholder.getTime();
				placeholder.updateTime(addTime);
				double raceTime = time + addTime;
				
				String name = placeholder.getName();
				
				String event = placeholder.getEvent();
				String team = placeholder.getTeam();
				
				DecimalFormat df = new DecimalFormat("#.##");
				
				System.out.println(name + "\t   " + df.format(time) + "\t" + df.format(addTime) + "\t" + df.format(raceTime));
				
				
				race.addResultsQueue(placeholder);
				
			}
		}
		
	}
	
	public void displayRaceResults(Race race) {
		System.out.println("\n\nFinal Scores\nName \t\t\tTime \t    Event \t\tTeam");
		while(race.isResultsQueueEmpty() == false) {
			Athlete placeholder = race.removeResultsQueue();
			System.out.println(placeholder.toString());
		}
	}

}

