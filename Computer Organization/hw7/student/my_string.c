/**
 * @file my_string.c
 * @author Jinlin Yang
 * @brief Your implementation of the famous string.h library functions!
 *
 * NOTE: NO ARRAY NOTATION IS ALLOWED IN THIS FILE
 *
 * @date 2024-03-xx
 */

#include "my_string.h"

/* Note about UNUSED_PARAM
*
* UNUSED_PARAM is used to avoid compiler warnings and errors regarding unused function
* parameters prior to implementing the function. Once you begin implementing this
* function, you can delete the UNUSED_PARAM lines.
*/

/**
 * @brief Calculate the length of a string
 *
 * @param s a constant C string
 * @return size_t the number of characters in the passed in string
 */
size_t my_strlen(const char *s)
{
    // size_t size = 0;
    // while (*s != '\0'){
    //     s++;
    //     size++;
    // }
    // return size;
    size_t i = 0;
    while (*(s + i) != '\0'){
        i++;
    }
    return i;
    return 5;
}

/**
 * @brief Compare two strings
 *
 * @param s1 First string to be compared
 * @param s2 Second string to be compared
 * @param n First (at most) n bytes to be compared
 * @return int representing the difference between the strings:
 *          - 0 if the strings are equal
 *          - arbitrary positive number if s1 > s2
 *          - arbitrary negative number if s1 < s2
 */
int my_strncmp(const char *s1, const char *s2, size_t n)
{
    // for (size_t i = 0; i <= n - 1; i++){
    //     if (*s1 != *s2){
    //         return *s1 - *s2;
    //     }
    //     if (*s1 == 0 || *s2 == 0){
    //         break;
    //     }
    //     s1++;
    //     s2++;
    // }
    for (size_t i = 0; i <= n - 1; i++){
        if (*(s1 + i) != *(s2 + i)){
            return *(s1 + i) - *(s2 + i);
        }
        if (*(s1 + i) == '\0' || *(s2 + i) == '\0'){ // this is a stupid bug, test cases are wrong
            break;
        }
    }
    return 0;
}

/**
 * @brief Copy a string
 *
 * @param dest The destination buffer
 * @param src The source to copy from
 * @param n maximum number of bytes to copy
 * @return char* a pointer same as dest
 */
char *my_strncpy(char *dest, const char *src, size_t n)
{
    // char *ans = dest; // if u do * dest then * dest will output the value at the address pointed by dest, which is the value of str[0]
    //                   // dest will output the value of dest, which is the address of src[0]
    // for (size_t i = 0; i <= n - 1; i++){
    //     *dest = *src;
    //     src++;
    //     dest++;
    // }
    // return ans;
    for (size_t i = 0; i <= n - 1; i++){
         *(dest + i) = *(src + i);
    }
    return dest;
}

/**
 * @brief Concatenates two strings and stores the result
 * in the destination string
 *
 * @param dest The destination string
 * @param src The source string
 * @param n The maximum number of bytes (or characters) from src to concatenate
 * @return char* a pointer same as dest
 */
char *my_strncat(char *dest, const char *src, size_t n)
{   
    char *p = dest;
    while (*p != '\0'){
        p++;
    }
    for (size_t i = 0; i <= n - 1; i++){
        if (*(src + i) == '\0'){
            break;
        }
        *(p + i) = *(src + i);
    }
    return dest;
    // char *str = dest;
    // while (*str != '\0'){
    //     str++;
    // }
    // for (size_t i = 0; i <= n - 1; i++){
    //    *str = *src;
    //    str++;
    //    src++;
    // }
    // return dest;
}

/**
 * @brief Copies the character c into the first n
 * bytes of memory starting at *str
 *
 * @param str The pointer to the block of memory to fill
 * @param c The character to fill in memory
 * @param n The number of bytes of memory to fill
 * @return char* a pointer same as str
 */
void *my_memset(void *str, int c, size_t n)
{   
    char* p = str;
    for (size_t i = 0; i <= n - 1; i++){
        *(p + i) = (char) c;
    }
    return str;
    // int is 4 bytes but each memory location is only 1 byte
    // we don't we increment i by 4?
    // is it because the value of c will not exceed a certain limit because it is used to present char
//     unsigned char *ptr = (unsigned char *)str;
//    for (size_t i = 0; i <= n - 1; i++){
//         *ptr = c;
//         ptr++;
//    }
//     return str;
}

/**
 * @brief Checks whether the string is a palindrome
 * (i.e., reads the same forwards and backwards)
 * assuming that the case of letters is irrelevant.
 * 
 * @param str The pointer to the string
 * @return 1 if the string is a palindrome,
 * or 0 if the string is not
*/
int is_palindrome_ignore_case(const char *str) 
{
    if (*str == '\0'){
        return 1;
    }
    for (size_t i = 0, j = my_strlen(str) - 1; i <= my_strlen(str) / 2 - 1; i++, j--){
        if (*(str + i) != *(str + j) && *(str + i) != *(str+j) + 32 && *(str+i) != *(str+j) - 32){
            return 0;
        }
    }
    return 1;
    // 0 1 2 3: 3 / 2 = 1
    // 0 1 2 3 4 : 4 / 2 = 2
    // if (my_strlen(str) == '\0'){
    //     return 1;
    // }
    // for (size_t i = 0, j = my_strlen(str) - 1; i <= (my_strlen(str) - 1) / 2; i++, j--){
    //     if (*(str+i) != *(str+j) && *(str+i) != *(str+j) + 32 && *(str+i) != *(str+j) - 32){
    //         return 0;
    //     }
    // }
    // return 1;
}

/**
 * @brief Apply a Caesar shift to each character
 * of the provided string in place.
 * 
 * @param str The pointer to the string
 * @param shift The amount to shift by
*/
void caesar_shift(char *str, int shift) 
{
    char *ptr = str;
    while (*ptr != '\0'){
        char hold = *ptr;
        if (hold >= 'A' && hold <= 'Z'){
            hold = 'A' + (hold - 'A' + shift) % 26; // how to come up with the formula
        } else if (hold >= 'a' && hold <= 'z'){
            hold = 'a' + (hold - 'a' + shift) % 26;
        }
        *ptr = hold;
        ptr++;
    }
}
/**
 * @brief Mutate the string in-place to
 * remove duplicate characters that appear
 * consecutively in the string.
 * 
 * @param str The pointer to the string
*/
void deduplicate_str(char *str) 
{   
    if (*str != '\0'){
        for (size_t i = 0; i <= my_strlen(str) - 2; i++){
        if (*(str + i) == *(str + i + 1)){
            for (size_t j = i + 1; j <= my_strlen(str) - 1; j++){
                *(str + j) = *(str + j + 1);
            }
            i--;
        }
    }
    }















    // char *ptr = str;
    // while (*ptr != '\0'){
    //     if (*ptr == *(ptr + 1)){
    //         char *ptr1 = ptr + 1;
    //         while (*ptr1 != '\0'){
    //             *(ptr1 - 1) = *ptr1;
    //             ptr1++;
    //         }
    //         *(ptr1 - 1) = '\0';
    //     } else {
    //         ptr++;
    //     }
    // }
}

/**
 * @brief Swap the position of
 * two strings in memory.
 * 
 * @param s1 The first string
 * @param s2 The second string
*/
void swap_strings(char **s1, char **s2) 
{
    char* tmp = *s1;
    *s1 = *s2; 
    *s2 = tmp;



















   //s1 = address of pointer pointed to the s1[0]
   //s2 = address of pointer pointed to the s2[0]
   //*s1 = address of s1[0];
   //*s2 = address of s2[0];
   //**s1 = value of s1[0];
   //**s2 = value of s2[0];
//    char *temp = *s1; 
//     *s1 = *s2;   
//     *s2 = temp;  



}