const { merge } = require('webpack-merge');
const common = require('./webpack.config.js');
const Dotenv = require('dotenv-webpack');
const dotenv = require('dotenv');

module.exports = () => {
  dotenv.config({ path: './.env.local' });

  return merge(common, {
    plugins: [
      new Dotenv({
        path: './.env.local',
      }),
    ],
    mode: 'development',
    devtool: 'eval-source-map',
    devServer: {
      historyApiFallback: true,
      port: 'auto',
      hot: true,
      proxy: [
        {
          context: ['/api'],
          target: process.env.REACT_APP_CRURU_API_URL,
          pathRewrite: { '^/api': '' },
          changeOrigin: true,
        },
      ],
    },
  });
};
