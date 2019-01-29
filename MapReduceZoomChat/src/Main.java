
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;

public class Main {
	
	public static void main(String args[]) throws Exception {
		
		// Create configuration
		Configuration conf = new Configuration();
		String[] otherArgs = new GenericOptionsParser(conf, args)
		.getRemainingArgs();
		
		//System.out.println(otherArgs[0] + " " + otherArgs[1] + " " + otherArgs[2]);
		
		// Parse arguments
		Parameter param = null;
		String userName = null;
		boolean success = false;
		
		//Command with three arguments
		if (otherArgs.length == 3) {

			switch (otherArgs[0].toLowerCase()) {
			case "user":
				param = Parameter.USER;
				break;
			case "time":
				param = Parameter.TIME;
				break;
			case "private":
				param = Parameter.PRIVATE;
				break;
			case "public":
				param = Parameter.PUBLIC;
				break;
			default:
				System.err.println("Invalid paramaters for MapReduce");
				System.exit(2);
			}
			
			//Run the MapReduce Job
			success = PostSort.sort(new Job(conf, "sort"), otherArgs[1], otherArgs[2], param, userName);
		}
		
		//Command with 4 arguments
		else {
			
			//Consider the parameter the 3rd last element since the name
			//argument could contain a first and last name and is separated by white space.
			switch (otherArgs[otherArgs.length - 3].toLowerCase()) {
				case "private":
					param = Parameter.PRIVATE;
					for (int i = 0; i < otherArgs.length - 3; i++)
						userName += otherArgs[i].toLowerCase() + " ";
					
					//Offset by 4 since there is "null" at the beginning
					userName = userName.substring(4, userName.length()-1);
					break;
				case "public":
					param = Parameter.PUBLIC;
					for (int i = 0; i < otherArgs.length - 3; i++)
						userName += otherArgs[i].toLowerCase() + " ";
					userName = userName.substring(4, userName.length()-1);
					break;
				case "both":
					param = Parameter.BOTH;
					for (int i = 0; i < otherArgs.length - 3; i++)
						userName += otherArgs[i].toLowerCase() + " ";
					userName = userName.substring(4, userName.length()-1);
					break;
				default:
					System.err.println("Invalid paramaters for MapReduce");
					System.exit(2);
			}
			
			//Run the MapReduce Job
			success = PostSort.sort(new Job(conf, "sort"), otherArgs[otherArgs.length-2], otherArgs[otherArgs.length-1], param, userName);
		}
		
		//Check to see if the job succeeded 
		System.exit(success ? 0 : 1);
	}
}
