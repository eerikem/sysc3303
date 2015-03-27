package voteserver;

public class Candidate {
		String name;
		String location;
		public Candidate (String Name, String Location){
			name = Name;
			location = Location;
		}
		
		public String getName (){
			return name;
		}
		
		public String getLocation(){
			return location;
		}

}
