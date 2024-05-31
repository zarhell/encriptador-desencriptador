package com.encriptadordesencriptador.domain.service;

import com.encriptadordesencriptador.application.port.Cipher;

public class TranspositionCipherService implements Cipher {

    @Override
    public String encrypt(String text) {
        int cols = (int) Math.ceil(Math.sqrt(text.length()));
        char[][] grid = new char[cols][cols];
        int index = 0;

        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < cols; j++) {
                if (index < text.length()) {
                    grid[i][j] = text.charAt(index++);
                } else {
                    grid[i][j] = ' ';
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < cols; i++) {
                result.append(grid[i][j]);
            }
        }

        return result.toString();
    }

    @Override
    public String decrypt(String text) {
        int cols = (int) Math.ceil(Math.sqrt(text.length()));
        char[][] grid = new char[cols][cols];
        int index = 0;

        for (int j = 0; j < cols; j++) {
            for (int i = 0; i < cols; i++) {
                if (index < text.length()) {
                    grid[i][j] = text.charAt(index++);
                } else {
                    grid[i][j] = ' ';
                }
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < cols; j++) {
                result.append(grid[i][j]);
            }
        }

        return result.toString().trim();
    }
}
