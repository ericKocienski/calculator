
 * Author: Eric Kocienski
 * finished May 5, 2019
 *  
 * make the equivalent of the windows 10 programming calculator in java
 * 
 * my calculator is able the follow the order of operations as well as perform real-time conversions between dec, bin, hex, and oct numbers
 * signed numbers are represented in 2s complement notation
 * possible operations are: add, subtract, multiply, divide, mod
 * you are able to use parenthesis 
 * 
 * QWORD, DWORD, WORD, and BYTE buttons and modes are implemented, the mode will restrict what you can enter in the calculator mainField, 
 * pressing QWORD, DWORD, WORD, and BYTE will change the number in mainField to fit the constraints. Math results in these modes will fir constraints
 * 
 * input validation is in place so that you cannot break the calculator. e.g: cant place more ) than (, cant put two operators next to each together without 
 * any operands, etc
 * 
 * font will change size as number in mainfield gets bigger, binary text will wrap in binField
 * 
 * includes custom icons to maintain the proper look and feel
