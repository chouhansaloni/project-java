package com.music;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

 class Songs {
    String title;
    String artist;
    String path;
    int duration;
    static Connection con= ConnectionClassForDM.getConnection();

// add song to database:
    public Songs(String title, String artist,int duration,String path){
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.path = path;
    }
    public static boolean AddSongToDB(String songName, String artistName, String path){

        // Scanner sc = new Scanner(System.in);
        // System.out.print("Enter song name: ");
        // String songName = sc.nextLine();
        // System.out.print("Enter Artist name: ");
        // String artistName = sc.nextLine();
        // System.out.print("Enetr song path: ");
        // String path =sc.nextLine();

        try{
            Statement stat = con.createStatement();
            String qury = "insert into AllSongs(Title,Artist,Path) values('"+songName+"','"+artistName+"','"+path+"')";
            // Execution:
            if(stat.executeUpdate(qury)==1){
            System.out.println("Song Added!");
            return true;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

// get all the song name from the database:
    public static boolean getAllSongs(){
        try{
            String query = "select*from AllSongs";
        PreparedStatement stat = con.prepareStatement(query);
        ResultSet rs = stat.executeQuery();
            while(rs.next()){
                String title = rs.getString("Title");
                System.out.println(title);
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

// get the name of a particular song:

    public static String particularSong(String title){
        try{
            String query = "Select*from AllSongs where Title= '"+title+"'";
            PreparedStatement stat = con.prepareStatement(query);
            ResultSet rs = stat.executeQuery();

        while(rs.next()){
            String Sname = rs.getString("title");
                String artist = rs.getString("Artist");
                String path = rs.getString("path");
                return "Song_name: "+Sname+"| Artist_name: "+artist+"| File_Path: "+path;
        }
    }catch(Exception e){
        e.printStackTrace();
    }
    return null;
    }

//  toString method:
@Override
    public String toString(){
    return "Song_Name: "+title+" Artist_Name: "+artist+" file_path: "+path;
    }
public String gettitle() {
    throw new UnsupportedOperationException("Unimplemented method 'gettitle'");
}
public String getartist() {
    throw new UnsupportedOperationException("Unimplemented method 'getartist'");
}
}
