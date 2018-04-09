# File_Deduplicator
EC504 course project

First Version:

i. Implement hashing method to go over all the files in the folder. Using a doubly linked list to represent the relationship between each words. Completed to produce a hashmap which  which stores all the unique segments of all the files. The key is the first word of the segment, and value is the linked list represents the segment. We call this map “head_map”. 
 
ii.Serialize the head_map and store it as binary file in disk for future use. We now able to acquire the serialized file and deserialize it back to hashmap at any time. 

iii.Compiling part: Go over each file again, when we encountered a word in the head_map, we store that word, otherwise ignore it. Then we replace the origin file with the file which contains all the “head word”, this is mainly to store the order of each segment.

iv. Recover:  When we want to recover this file, we go over the compiled file, and according to the stored keyword and head_map we store, we can recover the whole file. 
problem :
 If the file is very big and is splited into very small segments, which in the worst case, each segment only contains on word, its just to restore the original file again. And might not meet the requirement. Test the code with a file of 1M, it will takes a long time, and barely reduce storage.

Second version:

In this version we use a simpler idea, that is put each paragraph into a hash map, if it is unique, then we write this paragragh to the complie folder, and replace this paragraph with the name of the new file. If this paragraph is already in the map before, we just get the file name of this paragraph, and replace it. So if the files in the folder are nearly the same, it will reduce a lot of storage.

File before goes into the locker:(two 1M txt file)

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%207.53.30%20PM.png" width = "375" height = "273" alt="Laptop" align=center />

File after being locked:(reduced to 10kB each file, and a folder of 505kB, which is reduced to 525 kB in total)

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%207.53.14%20PM.png" width = "375" height = "273" alt="Laptop" align=center />

File recovered:(back to 1M and same to the original one)

<img src="https://github.com/AmyWangJingjun/File_Deduplicator/blob/master/version%202/ScreenShots/Screen%20Shot%202018-04-08%20at%208.40.48%20PM.png" width = "375" height = "273" alt="Laptop" align=center />

