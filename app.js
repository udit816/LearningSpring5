const express = require('express');
const AWS = require('aws-sdk');
const app = express();

// Configure AWS SDK
AWS.config.update({
  accessKeyId: 'AKIA5OEYDYQOKGDGXQK7',
  secretAccessKey: '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',
  region: 'us-east-1'
});

// API endpoint to read an image from S3
app.get('/image', async (req, res) => {
  try {
    const s3 = new AWS.S3();
    const params = {
      Bucket: 'merabucket2023',
      Key: 'sky.jpg'
    };

    const data = await s3.getObject(params).promise();
    res.set('Content-Type', data.ContentType);
    res.send(data.Body);
  } catch (error) {
    console.error('Error reading image from S3:', error);
    res.status(500).send('Error reading image from S3');
  }
});

// Start the server
const port = 3000; // or any port number you prefer
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});
