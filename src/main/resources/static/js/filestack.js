const client = filestack.init(FILESTACK_TOKEN);

const uploadDone = (results) => {
    const fileData = results.filesUploaded[0];
    imageUrl.value = fileData.url;
    document.getElementById("filestack").innerHTML = "Change Image";
}

const uploadFailed = (results) => {
    return "There was a problem uploading your file.";
}

const filestackOptions = {
    onUploadDone: uploadDone,
    onFileUploadFailed: uploadFailed,
    accept: ['image/*']
};
const imageUrl = document.getElementById("image_url");

$("#filestack").click(() => {
    client.picker(filestackOptions).open();
})
