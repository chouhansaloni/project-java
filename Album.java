package com.music;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Album{
     Connection con = ConnectionClassForDM.getConnection();
     Scanner sc;
    public Album(){ }
    public void viewAllAlbum(){
         try {
            System.out.println();
            String sql = "select AlbumName from Album";
            PreparedStatement stat = con.prepareStatement(sql);
           
            ResultSet rs = stat.executeQuery();
            while(rs.next()){
                System.out.println(rs.getString("AlbumName"));
            }
    }catch(Exception e){
        System.out.println("Album Not Found!");
    }
}


public void viewAlbumSongs(){
    sc = new Scanner(System.in);
     System.out.print("Enter Album name: ");
        String albumName = sc.nextLine();
        try {

 String qur ="SELECT Album.AlbumName, AllSongs.SongID, AllSongs.Title, AllSongs.Artist, AllSongs.Path FROM Album INNER JOIN AlbumSongs ON Album.AlbumID =AlbumSongs.AlbumID INNER JOIN AllSongs ON AlbumSongs.SongID = AllSongs.SongID";
            PreparedStatement statement = con.prepareStatement(qur);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                if(albumName.equals(rs.getString("AlbumName"))){
                String title = rs.getString("Title");
                String artist = rs.getString("Artist");
            
                System.out.println("Title: " + title +" | Artist: " + artist);
            }
        }
        } catch (SQLException e) {
            System.out.println("Error fetching songs.");
            e.printStackTrace();
        }
    }
    public void playAlbumSong() {
        sc = new Scanner(System.in);
        System.out.print("Enter Album name: ");
        String albumName = sc.nextLine();
    
        try {
            // Query to fetch songs from the specified album
            String sql = "SELECT Album.AlbumName, Album.AlbumID, AllSongs.SongID, AllSongs.Title, AllSongs.Artist, AllSongs.Path "
                       + "FROM Album "
                       + "INNER JOIN AlbumSongs ON Album.AlbumID = AlbumSongs.AlbumID "
                       + "INNER JOIN AllSongs ON AlbumSongs.SongID = AllSongs.SongID";
    
            PreparedStatement statement = con.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
    
            boolean songFound = false;
            while (rs.next()) {
                if (albumName.equals(rs.getString("AlbumName"))) {
                    String title = rs.getString("Title");
                    String artist = rs.getString("Artist");
                    String path = rs.getString("Path");
    
                    System.out.println("Playing Song: " + title + " | Artist: " + artist);
    
                    // Play the song using DemoMusicAudio
                    MusicAudio.playSound(path);
    
                    // Show song menu to allow users to control playback (Next, Previous, Quit)
                    PlaySong.showSongMenu();
    
                    songFound = true;
                    break; // Stop after playing the first song in the album
                }
            }
    
            if (!songFound) {
                System.out.println("No songs found in the specified album.");
            }
    
        } catch (SQLException e) {
            System.out.println("Error fetching song.");
            e.printStackTrace();
        }
    }
}
