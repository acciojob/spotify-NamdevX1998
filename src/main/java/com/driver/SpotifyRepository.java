package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class SpotifyRepository {
    public HashMap<Artist, List<Album>> artistAlbumMap;
    public HashMap<Album, List<Song>> albumSongMap;
    public HashMap<Playlist, List<Song>> playlistSongMap;
    public HashMap<Playlist, List<User>> playlistListenerMap;
    public HashMap<User, Playlist> creatorPlaylistMap;
    public HashMap<User, List<Playlist>> userPlaylistMap;
    public HashMap<Song, List<User>> songLikeMap;

    public List<User> users;
    public List<Song> songs;
    public List<Playlist> playlists;
    public List<Album> albums;
    public List<Artist> artists;

    public SpotifyRepository(){
        //To avoid hitting apis multiple times, initialize all the hashmaps here with some dummy data
        artistAlbumMap = new HashMap<>();
        albumSongMap = new HashMap<>();
        playlistSongMap = new HashMap<>();
        playlistListenerMap = new HashMap<>();
        creatorPlaylistMap = new HashMap<>();
        userPlaylistMap = new HashMap<>();
        songLikeMap = new HashMap<>();

        users = new ArrayList<>();
        songs = new ArrayList<>();
        playlists = new ArrayList<>();
        albums = new ArrayList<>();
        artists = new ArrayList<>();
    }

    public User createUser(String name, String mobile) {
        User user=new User(name,mobile);
        users.add(user);
        return user;
    }

    public Artist createArtist(String name) {
        Artist artist=new Artist(name);
        artists.add(artist);
        return artist;
    }

    public Album createAlbum(String title, String artistName) {
        boolean flag=false;
        Artist artist=null;
        for(Artist art:artists){
            String name=art.getName();
            if(name.equalsIgnoreCase(artistName)){
                flag=true;
                artist=art;
                break;
            }
        }
        if(flag==false){
            artist=new Artist(artistName);
            artists.add(artist);
        }
        flag=false;
        Album album=null;
        for(Album al:albums){
            String Title=al.getTitle();
            if(title.equalsIgnoreCase(Title)){
                flag=true;
                album=al;
                break;
            }
        }
        if(flag==false){
            album=new Album(title);
            albums.add(album);
        }
        if(artistAlbumMap.containsKey(artist)){
            List<Album>list=artistAlbumMap.get(artist);
            list.add(album);
            artistAlbumMap.put(artist,list);
            return album;
        }
        List<Album>list=new ArrayList<>();
        list.add(album);
        artistAlbumMap.put(artist,list);
        return album;
    }

    public Song createSong(String title, String albumName, int length) throws Exception{
        boolean flag=false;
        Album album=null;
        for(Album al:albums){
            String name=al.getTitle();
            if(name.equalsIgnoreCase(albumName)){
                flag=true;
                album=al;
                break;
            }
        }
        try{
            if(flag==false){
                throw new Exception("Album does not exist");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        Song s=new Song(title,length);
        songs.add(s);
        if(albumSongMap.containsKey(album)){
            List<Song>list=albumSongMap.get(album);
            list.add(s);
            albumSongMap.put(album,list);
            return s;
        }
        List<Song>list=new ArrayList<>();
        list.add(s);
        albumSongMap.put(album,list);
        return  s;
    }

    public Playlist createPlaylistOnLength(String mobile, String title, int length) throws Exception {
        boolean flag=false;
        for(User user:users){
            String mob= user.getMobile();
            if(mob.equalsIgnoreCase(mobile)){
                flag=true;
                break;
            }
        }
        try{
            if(flag==false){
                throw new Exception("User does not exist");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        Playlist play=new Playlist(title);
        playlists.add(play);
        List<Song>list=new ArrayList<>();
        for(Song s:songs){
            int len=s.getLength();
            if(len==length){
                list.add(s);
            }
        }
        playlistSongMap.put(play,list);
        return play;
    }

    public Playlist createPlaylistOnName(String mobile, String title, List<String> songTitles) throws Exception {
        boolean flag=false;
        for(User user:users){
            String mob= user.getMobile();
            if(mob.equalsIgnoreCase(mobile)){
                flag=true;
                break;
            }
        }
        try{
            if(flag==false){
                throw new Exception("User does not exist");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        Playlist play=new Playlist(title);
        playlists.add(play);
        List<Song>list=new ArrayList<>();
        for(String str:songTitles){
            for(Song s:songs){
                String S=s.getTitle();
                if(S.equalsIgnoreCase(str)){
                    list.add(s);
                    break;
                }
            }
        }
        playlistSongMap.put(play,list);
        return play;
    }

    public Playlist findPlaylist(String mobile, String playlistTitle) throws Exception {
        boolean flag=false;
        User userr=null;
        for(User user:users){
            String mob= user.getMobile();
            if(mob.equalsIgnoreCase(mobile)){
                flag=true;
                userr=user;
                break;
            }
        }
        try{
            if(flag==false){
                throw new Exception("User does not exist");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        flag=false;
        for(Playlist p:playlists){
            String str=p.getTitle();
            if(str.equalsIgnoreCase(playlistTitle)){
                flag=true;
                break;
            }
        }
        try{
            if(flag==false){
                throw new Exception("Playlist does not exist");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        Playlist playlist=null;
        for(Playlist play:playlists){
            String str=play.getTitle();
            if(str.equalsIgnoreCase(playlistTitle)){
                playlist=play;
            }
        }
        if(creatorPlaylistMap.containsKey(userr)==false){
            creatorPlaylistMap.put(userr,playlist);
        }
        if(playlistListenerMap.containsKey(playlist)){
            List<User>list=playlistListenerMap.get(playlist);
            list.add(userr);
            playlistListenerMap.put(playlist,list);
        }
        else{
            List<User>list=new ArrayList<>();
            list.add(userr);
            playlistListenerMap.put(playlist,list);
        }
        if(userPlaylistMap.containsKey(userr)){
            List<Playlist>list=userPlaylistMap.get(userr);
            list.add(playlist);
            userPlaylistMap.put(userr,list);
        }
        else{
            List<Playlist>list=new ArrayList<>();
            list.add(playlist);
            userPlaylistMap.put(userr,list);
        }
        return playlist;
    }

    public Song likeSong(String mobile, String songTitle) throws Exception {
        boolean flag=false;
        User userr=null;
        for(User user:users){
            String mob= user.getMobile();
            if(mob.equalsIgnoreCase(mobile)){
                flag=true;
                userr=user;
                break;
            }
        }
        try{
            if(flag==false){
                throw new Exception("User does not exist");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        flag=false;
        Song song=null;
        for(Song s:songs) {
            String str = s.getTitle();
            if(str.equalsIgnoreCase(songTitle)){
                flag=true;
                song=s;
                break;
            }
        }
        try{
            if(flag==false){
                throw new Exception("Song does not exist");
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
            return null;
        }
        song.setLikes(1);
        for(Artist art:artistAlbumMap.keySet()){
            List<Album>list=artistAlbumMap.get(art);
            for(Album album:list){
                for(Song s:albumSongMap.get(album)){
                    String title=s.getTitle();
                    if(title.equalsIgnoreCase(songTitle)){
                        art.setLikes(art.getLikes()+1);
                    }
                }
            }
        }
        if(songLikeMap.containsKey(song)){
            List<User>users1=songLikeMap.get(song);
            users1.add(userr);
            songLikeMap.put(song,users1);
        }
        else{
            List<User>users1=new ArrayList<>();
            users1.add(userr);
            songLikeMap.put(song,users1);
        }
        return song;
    }

    public String mostPopularArtist() {
        String name="";
        int max=0;
        for(Artist art:artists){
            int likes=art.getLikes();
            if(max<likes){
                max=likes;
                name=art.getName();
            }
        }
        return name;
    }

    public String mostPopularSong() {
        String name="";
        int max=0;
        for(Song s:songs){
            int likes=s.getLikes();
            if(max<likes){
                max=likes;
                name=s.getTitle();
            }
        }
        return name;
    }
}
