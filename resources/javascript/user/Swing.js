// Given a string str, the task is to find the length of the longest palindromic subsequence in the given string.
// Examples:
// Input: str = "c"
// Output: 1
// Input: str = "bbbab"
// Output: 4
// Explanation: The longest palindromic subsequence is "bbbb".
//
// Input: str = "cbbd"
// Output: 2
// Explanation: The longest palindromic subsequence is "bb".
//
// Approach: The idea is to use Dynamic Programming to solve this problem. Below are the steps:
// - Create a 2D array dp[][] of size n*n.
// - Iterate over the range [0, n-1] using the variable i and fill the first row of dp[][] with 1.
// - Iterate over the range [0, n-1] using the variable j and fill the first column of dp[][] with 1.
// - Iterate over the range [1, n-1] using the variable i and perform the following steps:
// -- Iterate over the range [1, n-1] using the variable j and perform the following steps:
// --- If str[i] == str[j], then dp[i][j] = dp[i-1][j-1] + 2.
// --- Otherwise, dp[i][j] = max(dp[i-1][j], dp[i][j-1]).
//
// - After completing the above steps, print the value of dp[n-1][n-1] as the result.
// Below is the implementation of the above approach:

<script>

// Javascript program for the above approach

// Function to find the length of
// the longest palindromic subsequence
function longestPalindromeSubseq(s)
{
    // Length of the string
    let n = s.length;

    // dp[i][j] will be true if the
    // string from index i to j is a
    // palindrome
    let dp =  new Array(n);
    for (let i = 0; i < n; i++) {
        dp[i] =  new Array(n);
    }
    for (let i = 0; i < n; i++) {
        for (let j = 0; j < n; j++) {
            dp[i][j] = 0;
        }
    }
    for (let i = 0; i < n; i++) {
        dp[i][i] = 1;
    }
    for (let i = 1; i < n; i++) {
        if (s[i] == s[i - 1]) {
            dp[i - 1][i] = 2;
        }
        else {
            dp[i - 1][i] = 1;
        }
    }

    for (let i = 3; i <= n; i++) {
        for (let j = 0; j < n - i + 1; j++) {
            let k = i + j - 1;
            if (s[j] == s[k]) {
            // If the first and last characters
            // are the same, then we add 2 to
            // the length of the palindromic
            // subsequence

            }else {
            // Otherwise, we take the maximum
            // of the lengths of the palindromic
            // subsequences that end with the
            // first and last characters
            }
        }
    }
    return dp[n - 1][n - 1];
}
// Driver Code
let s = "bbbab" ;
document.write(longestPalindromeSubseq(s));
