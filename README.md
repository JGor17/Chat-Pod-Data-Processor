# MapReduceZoomChat
using Hadoop to implement a MapReduce solution to sorting and organizing the chat pod of a zoom meeting



bin: Contains class files necessary to run the program
in: Folder that contains the csv file which is the file that contains all posts from the Zoom session
out: Output folder that contains the output file once the program is finished
src: Contains java files that implement the MapReduce solution:
    Main.java: Main class of the program. Takes in arguments upon building and runs the Hadoop job
    Parameter.java: Contains all enumerated constants. They are parameters that can be expected to be entered at Main
    Post.java: Instances of this class are Posts. Posts contain information such as:
        The time of its post, the user who posted it, the recipiant of the post, its visibility to other people, and the content of the post
    PostSort: Algorithm that contains the mapper and the reducer for the MapReduce framework. Creates a post for each incoming csv line
        and produces an output result to the "out" folder depending on which command was entered.
        
       
Background: 

This project was created by me during my time spent as an intern at 2U, which is an online service that partners with universities
to provide online classes through the use of LMS's and Zoom, which is a video conferencing platform in which professors can have live lectures with
their students. Most of my work involved helping students and faculty with tech-related issues and most of my interactions with them involved posts
using the chat pod on Zoom. Once I leave the session, I get a copy of the entire chat history sent to my Downloads folder so that I could look at it
for self-reflection. Looking at these files was tiresome and I wondered if there was a better way to sort and organize each post so that I
could speed up the time needed to look at certain posts and conversations that happened in the session I just attended. The solution was to
use Hadoop. That way, I can speed up the time necessary for self-reflection in between sessions so that I can improve myself for next time.

Brief Summary:
This program takes advantage of parallelism to sort given key value pairs as input. It takes a csv file as input and produces an output file that
is sorted. It has a few commands that are usefull for sorting and organizing the input file:

<user> <in> <out>: This tells the program to sort all the posts by user
<time> <in> <out>: Tells the program to sort all the posts by time
<private> <in> <out>: Display only private messages and sort them by time
<public> <in> <out>: Display only public messages and sort them by time
<"name"> <private/public/both> <in> <out>: Display only posts made by this user and sort them by time. These posts can be private, public, or both

Directions:

(This program runs on the most current version of Java and Eclipse)

First, download and install Hadoop 0.20.2

Next, create a new Java Project in Eclipse. Copy and paste the "in" folder and all the .java files in the "src" file into the newly created project,
then copy and paste the Hadoop folder into the project

Next, import these files into the java project:

    hadoop-0.20.2/hadoop-0.20.2-core.jar
    hadoop-0.20.2/lib/log4j-1.2.15.jar
    hadoop-0.20.2/lib/(all files with the prefix "commons")
    
To do this, right click on the java project and navigate to Build Path/Add External Archives, then select

Once the project is set up, navigate to the drop down next to the play button and click on Run Configurations.
In the Main tab, make sure that the project is set to MapReduceZoomChat and Main is set to Main from the MapReduceZoomChat.
Next navigate to the arguments tab and fill them with each argument (Note: each argument should be seperated by one space).
Hit "Apply" then run.

After the program stops running, refresh the project by right clicking on it and hitting "refresh." There should be a newly-created "out"
folder that contains the output file.

As an example I included in this repository of an actual session I shadowed at 2U (the names of all faculty and students have been changed to protect their privacy).
My command to the program was:

george washington university both in/saved_chat_example.csv out

This tells the program to show both private and public messages made by "george washington university," which is me, and sort them by time.





