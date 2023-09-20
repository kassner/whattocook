const path = require('path');
const autoprefixer = require('autoprefixer');
const MiniCssExtractPlugin = require("mini-css-extract-plugin");

module.exports = {
    entry: {
        app: './src/main/js/app/main.tsx',
        manage: './src/main/js/manage/main.ts',
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                use: 'ts-loader',
                exclude: /node_modules/,
            },
            {
                test: /\.(scss)$/,
                use: [
                    MiniCssExtractPlugin.loader,
                    {loader: 'css-loader'},
                    {
                        loader: 'postcss-loader',
                        options: {
                            postcssOptions: {
                                plugins: [autoprefixer]
                            }
                        }
                    },
                    {loader: 'sass-loader'}
                ]
            },
        ],
    },
    plugins: [
        new MiniCssExtractPlugin(),
    ],
    resolve: {
        extensions: ['.tsx', '.ts', '.js'],
    },
    output: {
        path: path.resolve('./src/main/resources/static/dist/'),
        publicPath: '/dist/',
        filename: '[name].js',
    },
    watchOptions: {
        aggregateTimeout: 1000,
        ignored: '**/node_modules/',
    },
};
