
import java.io.IOException;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class PostSort {
	
	private static Parameter myParam;
	private static String myUserName = null;
	
	public static class SwapMapper extends Mapper<LongWritable, Text, Text, Text> {

		@Override
		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			
			//Create post object
			Post post = Post.createPost(value.toString());
			
			//Figure out which command has been entered by referring to parameters
			//This is the case if there are 3 arguments that have been entered
			if (myUserName == null) {
				
				//<user> <in> <out>
				if (myParam == Parameter.USER)
					context.write(new Text(post.getUser()), new Text(post.getUser() + "::	" + "to: " + post.getRecipient() + " at: " + post.getTime() + ": " + post.getText()));
				
				//<time> <in> <out>
				else if (myParam == Parameter.TIME)
					context.write(new Text(post.getTime()), new Text(post.getTime() + "::	" + "from: " + post.getUser() + " to: " + post.getRecipient() + ": " + post.getText()));
				
				//<private> <in> <out>
				else if (myParam == Parameter.PRIVATE) {
					if (post.isPrivate())
						context.write(new Text(post.getTime()), new Text(post.getTime() + "::	" + "from: " + post.getUser() + " to: " + post.getRecipient() + ": " + post.getText()));
				}
				
				//<public> <in> <out>
				else {
					if (!post.isPrivate())
						context.write(new Text(post.getTime()), new Text(post.getTime() + "::	" + "from: " + post.getUser() + " to: " + post.getRecipient() + ": " + post.getText()));
				}
			}
			
			//This is the case if 4 arguments have been entered
			else {
				
				//<"first + last name"> <private> <in> <out>
				if (myParam == Parameter.PRIVATE) {
					if (post.isPrivate() && post.getUser().equals(myUserName))
						context.write(new Text(post.getTime()), new Text(post.getTime() + "::	" + "to: " + post.getRecipient() + ": " + post.getText()));
				}
				
				//<"first + last name"> <public> <in> <out>
				else if (myParam == Parameter.PUBLIC) {
					if (!post.isPrivate() && post.getUser().equals(myUserName))
						context.write(new Text(post.getTime()), new Text(post.getTime() + "::	" + "to: " + post.getRecipient() + ": " + post.getText()));
				}
				
				//<"first + last name"> <both> <in> <out>
				else {
					if (post.getUser().equals(myUserName))
						context.write(new Text(post.getTime()), new Text(post.getTime() + "::	" + "to: " + post.getRecipient() + ": " + post.getText()));
				}
			}
		}
	}
	
	public static class SwapReducer extends Reducer<Text, Text, Text, Text> {

		@Override
		public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
			
			//Hadoop sorts the keys in order, so all that's left to do is print each post out
			for (Text txt : values)
				
				//The key has been sorted and a text representation of it exists in values already.
				//There is no need to write the key since all we needed it for was for Hadoop
				//to sort each k/v pair, so a "new Text()" object is written instead,
				//since we don't want to write a value, but we need some kind of value whenever
				//we make a write.
				context.write(txt, new Text());
		}
	}
	
	public static boolean sort(Job job, String input, String output, Parameter param, String userName)
			throws Exception {

		myParam = param;
		myUserName = userName;
		job.setJarByClass(PostSort.class);

		//Set up map-reduce...
		
		job.setJobName("PostSort");
		job.setJarByClass(PostSort.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);
		
		job.setMapperClass(PostSort.SwapMapper.class);
		job.setReducerClass(PostSort.SwapReducer.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
		
		// Build pathway and wait for job to complete

		FileInputFormat.addInputPath(job, new Path(input));
		FileOutputFormat.setOutputPath(job, new Path(output));

		return job.waitForCompletion(true);
	}
}
