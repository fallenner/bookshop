const HtmlWebpackPlugin = require('html-webpack-plugin');
// 引用这个库主要是解决windows下的路径字符串问题
const path = require('path');

module.exports = {
  entry: './src/react/index.tsx',
  module: {
    rules: [
      {
        test: /\.(ts|js)x?$/,
        use: 'babel-loader',
        exclude: /node_modules/,
      },
      {
        test: /\.less$/i,
        use: [
          {
            loader: 'style-loader', // creates style nodes from JS strings
          },
          {
            loader: 'css-loader', // translates CSS into CommonJS
            options: {
              modules: {
                mode: (resourcePath) => {
                  if (
                    resourcePath.includes('global.css') ||
                    resourcePath.includes('iconfont.css')
                  ) {
                    return 'global';
                  }
                  return 'local';
                },
                localIdentName: '[local]__[hash:base64:6]',
                localIdentHashSalt: 'book_shop',
              },
            },
          },
          {
            loader: 'less-loader', // compiles Less to CSS
          },
        ],
      },
      {
        test: /\.(png|jpe?g|gif|svg|eot|ttf|woff|woff2)$/i,
        use: [
          {
            loader: 'url-loader',
            options: {
              limit: 8192,
            },
          },
        ],
      },
    ],
  },
  resolve: {
    extensions: ['.tsx', '.jsx', '.ts', '.js'],
    alias: {
      '@/assets': path.resolve(__dirname, 'src/assets'),
    },
  },
  plugins: [new HtmlWebpackPlugin({ title: 'BookShop' })],
  devtool: 'inline-source-map',
  devServer: {
    port: 3000,
  },
};
