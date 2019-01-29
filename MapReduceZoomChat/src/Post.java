
public class Post {
	
	private String time;
	private String user;
	private String recipient;
	private String text;
	private boolean isPrivate;
	
	private Post() {}
	
	public String getTime() {
		return time;
	}
	
	public String getUser() {
		return user;
	}
	
	public String getRecipient() {
		return ((recipient == null)? "EVERYONE" : recipient);
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}
	
	public static Post createPost(String csvLine) {
		
		Post post = new Post();
		String[] words = csvLine.split(",");
		String[] conversation = words[3].split(" ");
		
		//Set time
		String hours = ((words[0].length() == 1)? "0" + words[0] : words[0]);
		String minutes = ((words[1].length() == 1)? "0" + words[1] : words[1]);
		String seconds = ((words[2].length() == 1)? "0" + words[2] : words[2]);
		post.time = hours + ":" + minutes + ":" + seconds;
		
		//Set isPrivate
		if (conversation[conversation.length-1].contains("(Privately)")) {
			post.isPrivate = true;
			conversation[conversation.length-1] = conversation[conversation.length-1].substring(0, conversation[conversation.length-1].length() - 11);
			
			//Set user and recipient for private post
			//offset by 2 since there is a null character and the word "From" at the start
			for (int i = 2; i < conversation.length; i++) {
				if (conversation[i].equals("to")) {
					for (int j = i+1; j < conversation.length; j++) {
						post.recipient += " " + conversation[j];
					}
					break;
				}
				else {
					post.user += " " + conversation[i];
				}
			}
			//Ignore the first 6 characters since each start with a "null"
			//Also ignore the last character when storing the user since it is just
			//trailing white space.
			post.user = post.user.substring(6,post.user.length()-1).toLowerCase();
			post.recipient = post.recipient.substring(6).toLowerCase();
			
		}
		else {
			post.isPrivate = false;
			
			//Set user for public post
			//Start at 2 to avoid capturing the "from"
			for (int i = 2; i < conversation.length; i++) {
				post.user += " " + conversation[i];
			}
			//Ignore the fist 6 characters since they are "null"
			post.user = post.user.substring(6).toLowerCase();
			post.recipient = null;
		}
	
		//Set text
		post.text = words[4];
		
		return post;
	}
	
	@Override
	public String toString() {
		return "time:" + time + "	" + "user: " + user + "	" + "recipient: " + recipient + "	" + "text: " + text + "	" + "visibility: " + (isPrivate ? "private" : "public");
	}
}
