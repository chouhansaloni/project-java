package com.music ;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

class Playlist {
    Scanner scanner = new Scanner(System.in);
    Connection connection;
    User loggedInUser;
    boolean loggedIn = true;

    // Constructor to initialize the connection and user
    public Playlist(User loggedInUser) {
        this.connection = ConnectionClassForDM.getConnection(); // Get connection from the connection class
        this.loggedInUser = loggedInUser; // Pass the logged-in user
    }

    public void userMenu() {
        while (loggedIn) {
            System.out.println("\nUser Menu:");
            System.out.println("1. Play A Song");
            System.out.println("2. Create Playlist");
            System.out.println("3. View All Playlists");
            System.out.println("4. Add Song to Playlist");
            System.out.println("5. View Songs in Playlist");
            System.out.println("6. Play Song From Playlist");
            System.out.println("7. View All Album");
            System.out.println("8. View Album Songs");
            System.out.println("9. Play Album Songs");
            System.out.println("10. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            Album ad = new Album();
            switch (choice) {
                case 1:
                
                try{
                String qry = "Select SongID, Title, Path from AllSongs";
                PreparedStatement pst = connection.prepareStatement(qry);
                ResultSet rs = pst.executeQuery();
                while(rs.next()){
                    System.out.println(rs.getString("Title"));
                  //  list.add(rs.getString("Path"));
                }
                System.out.print("Select a song name from the following song list: ");
                    String name = scanner.nextLine();
                    PlaySong.playSong(name);
                }catch(Exception e){
                    System.out.println("Couldn't find the songs!");
                }
                break;

                case 2:
                    createPlaylist();
                    break;
                case 3:
                    viewPlaylists();
                    break;
                case 4:
                    addSongToPlaylist();
                    break;
                case 5:
                    viewSongsInPlaylist();
                    break;
                case 6:
                    playSongInPlaylist();
                    break;
                case 7: // view Album
                    ad.viewAllAlbum();
                    break;
                case 8: // view album songs
                    ad.viewAlbumSongs();
                    break;
                case 9:     // play songs from album
                    ad.playAlbumSong();
                    break;
                case 10:
                    loggedIn = false;
                     System.out.println("Logged out successfully.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void createPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = scanner.nextLine();

        try {
            // Corrected table and column names based on the database schema
            String sql = "INSERT INTO Playlists (PlaylistName, UserID) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, playlistName);
            statement.setInt(2, loggedInUser.getUserID()); // Use the logged-in user's ID
            statement.executeUpdate();
            System.out.println("Playlist created successfully!");
        } catch (SQLException e) {
            System.out.println("Error creating playlist.");
            e.printStackTrace();
        }
    }

    
    private void viewPlaylists() {
        try {
            // Corrected table and column names based on the database schema
            String sql = "SELECT * FROM Playlists WHERE UserID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, loggedInUser.getUserID()); // Use the logged-in user's ID
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int playlistId = resultSet.getInt("PlaylistID");
                String name = resultSet.getString("PlaylistName");
                System.out.println("Playlist ID: " + playlistId + ", Name: " + name);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching playlists.");
            e.printStackTrace();
        }
    }

    private void addSongToPlaylist() {
        // add song to exsisting playlist:
        
        System.out.print("Enter playlist Name: ");
                String playlistName = scanner.nextLine();
        System.out.print("Enter song name: ");
                String title = scanner.nextLine();
       
        try {
            // get PlaylistID from playlist table:
            
                String qur = "select PlaylistID from Playlists where playlistName ='"+playlistName+"'";
                int playlistID=0;
                PreparedStatement statement = connection.prepareStatement(qur);
                ResultSet rs = statement.executeQuery();
                while(rs.next()){
                     playlistID = rs.getInt("PlaylistID");
                }
            // get songID from song table:
            
                int songId =0;

                String qur1 = "select SongID from AllSongs where Title= '"+title+"'";
                statement = connection.prepareStatement(qur1);
                rs = statement.executeQuery();
                while(rs.next()){
                     songId = rs.getInt("SongID");
                }

                          // Corrected table and column names based on the database schema
            String sql = "INSERT INTO PlaylistsSongs (PlaylistID, SongID) VALUES (?, ?)";
             statement = connection.prepareStatement(sql);
             statement.setInt(1, playlistID);
             statement.setInt(2, songId);

           
            statement.executeUpdate();
            System.out.println("Song added to playlist successfully!");
        } catch (SQLException e) {
            System.out.println("Error adding song.");
            e.printStackTrace();
        }
    }


    private void viewSongsInPlaylist() {
        System.out.print("Enter playlist name: ");
        String playlistName = scanner.nextLine();

        try {

 String qur ="SELECT Playlists.PlaylistName, AllSongs.SongID, AllSongs.Title, AllSongs.Artist FROM Playlists INNER JOIN PlaylistsSongs ON Playlists.PlaylistId = PlaylistsSongs.PlaylistID INNER JOIN AllSongs ON PlaylistsSongs.SongID = AllSongs.SongID";
            PreparedStatement statement = connection.prepareStatement(qur);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                if(playlistName.equals(resultSet.getString("PlaylistName"))){
                int songId = resultSet.getInt("SongID");
                String title = resultSet.getString("Title");
                String artist = resultSet.getString("Artist");
            
                System.out.println("Song ID: " + songId +" | Title: " + title +" | Artist: " + artist);
            }
        }
        } catch (SQLException e) {
            System.out.println("Error fetching songs.");
            e.printStackTrace();
        }
    }
    private void playSongInPlaylist() {
        System.out.print("Enter Playlist name: ");
        String playlistName = scanner.nextLine();
    
        try {
            // Corrected table and column names based on the database schema
            String sql = " SELECT Playlists.PlaylistName, Playlists.PlaylistID, AllSongs.SongID, AllSongs.Title, AllSongs.Artist, AllSongs.Path "
                       + " FROM Playlists "
                       + " INNER JOIN PlaylistsSongs ON Playlists.PlaylistId = PlaylistsSongs.PlaylistID "
                       + " INNER JOIN AllSongs ON PlaylistsSongs.SongID = AllSongs.SongID";
    
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
    
            boolean songFound = false;
            while (resultSet.next()) {
                if (playlistName.equals(resultSet.getString("PlaylistName"))) {
                    int playlistId = resultSet.getInt("PlaylistID");
                    String title = resultSet.getString("Title");
                    String artist = resultSet.getString("Artist");
                    String path = resultSet.getString("Path");
    
                    System.out.println("PlaylistID: " + playlistId + " | Song: " + title + " | Artist: " + artist);
    
                    // Play the song
                    MusicAudio.playSound(path);
                    
                    // Call the showSongMenu to allow users to control the playback
                    PlaySong.showSongMenu();
    
                    songFound = true;
                    break; // Break after playing the first song found in the playlist
                }
            }
    
            if (!songFound) {
                System.out.println("Song not found in the playlist.");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching song.");
            e.printStackTrace();
        }
    }
} 
