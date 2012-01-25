
package WikiBot;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * 
 */
public class Page {
    //  private String username;

    public enum MESSAGETYPE {

        LIKE, DISLIKE, TITLE, COMMENT
    };
    private String object_Id, content, object_name;
    private String title, comment;
    
    // initializing user page
    public Page(String object_Id, String content, String object_name) {
        this.content = content;
        this.object_name = object_name;
        this.object_Id = object_Id;
    }

    //initializing object page
    public Page(String object_Id, String object_name) {
        this.object_Id = object_Id;
        this.object_name = object_name;
    }

    public void createObjectPage() {
        content = "{{ObjectPage}}";
    }

    public void createNewUserpage(String messageType) {
        Logger.getLogger(Page.class.getName()).log(Level.INFO, "Creating new userpage");
        content = content.concat("[[Category:User]]\n");
        create(messageType);
    }
    //Create like/dislike/title/comment pages

    public void create(String messageType) {

        switch (MESSAGETYPE.valueOf(messageType)) //checking if the previously liked/disliked 
        {
            case LIKE:
                check(MESSAGETYPE.DISLIKE.toString());
                break;
            case DISLIKE:
                check(MESSAGETYPE.LIKE.toString());
                break;
            default:
                break;
        }
        if (!content.contains("<!-- " + messageType.toUpperCase() + "_START_HERE -->")) {
            content = content.concat(getStartTag(messageType, true));
            content = content.concat(semanticSyntax(messageType));
            content = content.concat(getStartTag(messageType, false));

        } else {
            if (!content.toLowerCase().contains(semanticSyntax(messageType).toLowerCase())) {
                String splitContent, endContent;
                Logger.getLogger(Page.class.getName()).log(Level.INFO, "Appending " + messageType + " syntax to the page");
                int index = content.indexOf("<!-- " + messageType.toUpperCase() + "_START_HERE -->") + ("<!-- " + messageType.toUpperCase() + "_START_HERE -->").length();
                splitContent = content.substring(0, index);
                endContent = content.substring(index, content.length());
                splitContent = splitContent.concat("\n" + semanticSyntax(messageType));
                splitContent = splitContent.concat(endContent);
                content = splitContent;
            } else {
                Logger.getLogger(Page.class.getName()).log(Level.INFO, "MESSAGE ALREADY EXISTS :EXIT");
                System.exit(0);
            }
        }
    }
    /*
     * Checks if the user liking an object has beore disliked it or not ,and vice-versa
     */

    public void check(String messageType) {
        String splitContent;
        if (content.contains(semanticSyntax(messageType))) // if the gene is already liked/disliked 
        {
            int index = content.indexOf(semanticSyntax(messageType));// + semanticSyntax(messageType).length());
            int sum = semanticSyntax(messageType).length();
            index = index + sum;
            splitContent = content.substring(0, content.indexOf(semanticSyntax(messageType)));
            splitContent = splitContent.concat(content.substring(index));
            content = splitContent;
        } else {
            Logger.getLogger(Page.class.getName()).log(Level.INFO, "Does not ", messageType);
        }
    }

    public String getContent() {
        return content;
    }
    //   The syntax for like     [[Like::objectName:objectId]] 
    //              for title    [[suggestion::objectName:objectId]] : [[Title:objectName:objectId::title:: <new title>]]
    //              for comment  [[annotation::objectName:objectId]] : [[Comment:objectName:objectId::comment:: <comment>]]

    public String semanticSyntax(String messageType) {
        Logger.getLogger(Page.class.getName()).log(Level.INFO, "Creating semanticSyntax for the userpage");
        String semanticSyntax = "";
        String start = "[[";
        String col = ":";
        String end = "]]\n";
        if (messageType.equalsIgnoreCase(MESSAGETYPE.TITLE.toString())) //generate semantic syntax for TITLE
        {
            semanticSyntax = semanticSyntax.concat(start + "suggestion" + col + col + object_name + col + object_Id + "]]");
            semanticSyntax = semanticSyntax.concat(" " + col + " " + start + "Title:"+ object_name + col + object_Id + col + col + messageType.toLowerCase() + col + col + title + end);
        }
        else if(messageType.equalsIgnoreCase(MESSAGETYPE.COMMENT.toString()))
        {
            semanticSyntax = semanticSyntax.concat(start + "annotation" + col + col + object_name + col + object_Id + "]]");
            semanticSyntax = semanticSyntax.concat(" " + col + " " + start + "Comment:"+ object_name + col + object_Id + col + col + messageType.toLowerCase() + col + col + comment + end);
        }
        else// generate Semantic syntax for LIKE/DISLIKE
        {
            semanticSyntax = semanticSyntax.concat(start + messageType.toLowerCase() + col + col + object_name + col + object_Id + end);
        }
        /*
         * if(messageType.equalsIgnoreCase(MESSAGETYPE.COMMENT.toString())) 
         * generate syntax for COMMENT
         */
        return semanticSyntax;
    }

    public String getStartTag(String type, boolean begn) {
        Logger.getLogger(Page.class.getName()).log(Level.INFO, "Creating label");
        String data = "";
        String start;
        if (begn) {
            start = "START";
            if (type.equalsIgnoreCase(MESSAGETYPE.TITLE.toString())) {
                data = "== List of " + type + " suggetions ==";
            } else {
                data = "== List of " + type + " ==";
            }

        } else {
            start = "END";
        }
        data = data.concat("\n" + "<!-- " + type.toUpperCase() + "_" + start + "_HERE -->\n");
        return data;
    }
// sets the title suggested by the user

    public void setTitle(String title) {
        this.title = title;
    }
    //set the comment

    public void setComment(String comment) {
        this.comment = comment;
    }
}