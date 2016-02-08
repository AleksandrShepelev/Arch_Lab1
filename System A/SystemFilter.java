/******************************************************************************************************************
 * File: DataConverter.java
 * Course: Software Architecture
 * Project: Assignment 1
 * Copyright: SKB Kontur Team (MSIT SE)
 * Date: 08.02.16
 *
 * Description:
 *
 * This class represents the abstract class for our system specific system to work with our system specific data
 * All other filter implementations should inherit this class
 *
 *
 ******************************************************************************************************************/

public abstract class SystemFilter extends FilterFramework {
    protected static final int measurementLength = 8; // This is the length of all measurements (including time) in bytes
    protected static final int idLength = 4; // This is the length of IDs in the byte stream

    protected int bytesRead = 0; // Number of bytes read from the input file.
    protected int bytesWritten = 0; // Number of bytes written to the stream.

}
