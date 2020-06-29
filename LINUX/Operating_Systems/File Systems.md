# File Systems

## Storage Management and File Systems

- Maybe the most visible aspect of OS to users

- Main memory is usually too **small** to accommodate all the data and programs

- Main memory is **volatile**; data will be gone on reboot

- **File systems** implement an abstraction for secondary secondary storage that is **big** and **persistent**
  - Disks, SSDs, optical disks, USB drives, clouds, …

## Files

- **A named collection of related information that is recorded on secondary storage**
  - Persistent through power failures and system reboots

- OS provides a uniform logical view of information storage via files

<br>
<p align = 'center'>
<img width = '800' src = 'https://user-images.githubusercontent.com/39554623/85956014-6ef92280-b9bd-11ea-9795-d874724fc122.png'>
</p>
<br>

### File Attributes

- **Name** – only information kept in human-readable form
- **Identifier** – unique tag (number) identifies file within file system
- Type – needed for systems that support different types
- Location – pointer to file location on device
- Size – current file size
- Protection/Permission – controls who can do reading, writing, executing
- Time, date, and user identification – data for protection, security, and usage monitoring

### File Types

- File may have types
  - Executable, object, source file, scripts, text, word processor, library, archive, multimedia, links, …

- Windows prefers to encode types in name
  - Extension (.com, .exe, .bat, .dll, .jpg, .avi, .mp3, …)

- UNIX prefers to encode types in contents
  - Magic number (0xcafebabe for Java class files)
  - Initial characters (#! For shell scripts)

- Or mixed them up

### File Operations

- File is an abstract data type
  - Create a file
  - Write a file
  - Read a file
  - Reposition within file [Will be discussed later]
  - Delete
  - Truncate: Erase contents but keep attributes

## File Operations: Open

- OS manages open files in the system
  - **Open-file table** keeps the information of open files
    - File-open count
      - Counter of number of times a file is open
      - To remove record from open-file table when the last process closes it
    - Disk location of the file
    - Access rights: per-process access mode

- Each process has its own **file descriptor(fd) table** that keeps track of its own open files

## File Access

- Processes have a “file pointer” for its each open file
  - The last read/written location in the file

- read()/write() automatically changes the file pointer

- lseek() repositions the file pointer

## Directories ★

> **File의 이름을 Metadata로 mapping 시켜주는 역할을 한다.**

- [**A named collection of related files and directories**]()

- For users
  - Provide a structured way to organize files

- For the file system
  - Provide a consistent naming interface that allows the implementation to separate logical file organization from physical file placement on the disk

- [**Typically just a file that happens to contain special metadata**]()
  - **Metadata**: data that describes data

- Directory = List of (file name, **file attributes <sup>내부에 File Identifier가 존재</sup>**)
  - Attributes include;
  - File ID, location on disk, size, protection, various timestamps, …

- Usually unordered (effectively random)
  - Entries are usually sorted by program that reads directory (e.g., ls)

<br>
<p align = 'center'>
<img width = '700' src = 'https://user-images.githubusercontent.com/39554623/85956185-293d5980-b9bf-11ea-8e24-4b6d58823f1d.png'>
</p>
<br>

- Root directory
  - `/`

- Current directory (working directory)
  - `.`

- Parent directory
  - `..`

- Absolute path name vs. relative path name
  - `/etc/ssh/sshd_config`
  - `On /etc, ./ssh/sshd_config, ../etc/../etc/ssh/sshd_config`

- **When open(”/spell/mail/exp”, …)**
  - Open the root directory “/” (well known, can always find)
  - Search the directory for “spell”, get location of “spell”
  - Open directory “spell”, search for “mail”, get location of “mail”
  - Open directory “mail”, search for “exp”, get location of “exp”
  - Open file “exp”

- Systems spend a lot of time walking down directory paths
  - This is why **`open()` is separate from `read()`/`write()`**
  - OS caches prefix lookups to enhance performance