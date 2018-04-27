# File_Deduplicator
EC504 course project


## Changelog
### Version 2.1
- Fixed the bug that the recovered file does not match the original file.
- Programmed a sever can transfer files.

        $ nc "HOST_IP" 9527 >> "Directory_Where_You_Want_TO_Store_The_Downloaded_File"
        $ "File_Name_You_Want_To_Download_From_The_Server"

First Version:

i. Implement hashing method to go over all the files in the folder. Using a doubly linked list to represent the relationship between each words. Completed to produce a hashmap which  which stores all the unique segments of all the files. The key is the first word of the segment, and value is the linked list represents the segment. We call this map “head_map”. 
 
ii.Serialize the head_map and store it as binary file in disk for future use. We now able to acquire the serialized file and deserialize it back to hashmap at any time. 

iii.Compiling part: Go over each file again, when we encountered a word in the head_map, we store that word, otherwise ignore it. Then we replace the origin file with the file which contains all the “head word”, this is mainly to store the order of each segment.

iv. Recover:  When we want to recover this file, we go over the compiled file, and according to the stored keyword and head_map we store, we can recover the whole file. 
problem :
 If the file is very big and is splited into very small segments, which in the worst case, each segment only contains on word, its just to restore the original file again. And might not meet the requirement. Test the code with a file of 1M, it will takes a long time, and barely reduce storage.

Second version:

In this version we use a simpler idea, that is go through all the paragraphs of each file, put each them into a hashmap, whose key is the hash value of each paragragh, and value is the name of a new file, which contains continent of this paragraph. Then we rewrite the original file with names of the new created "paragraph file". So if the files in the folder are nearly the same, it will reduce a lot of storage.

File before goes into the locker:(two 1M txt file)

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%207.53.30%20PM.png" width = "375" height = "273" alt="Laptop" align=center />

File after being locked:(reduced to 10kB each file, and a folder of 505kB, which is reduced to 525 kB in total)

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%207.53.14%20PM.png" width = "375" height = "273" alt="Laptop" align=center />

File recovered:(back to 1M and same to the original one)

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%208.40.48%20PM.png" width = "375" height = "273" alt="Laptop" align=center />

Storage used before compile:

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%209.13.12%20PM.png" height = "273" alt="Laptop" align=center />

Storage used after complie:

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%209.13.48%20PM.png" height = "273" alt="Laptop" align=center />

