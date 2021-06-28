import boto3
client=boto3.client('rekognition')

s3 = boto3.resource('s3')

def lista_imagens():
    imagens = []
    bucket = s3.Bucket('fa-images1')
    for imagem in bucket.objects.all():
        imagens.append(imagem.key)
    #print(imagens)
    return imagens
#Indexar as faces: atribuir os nomes dos rostos nas faces. Vai pegar todas as fotos o bucket e atribuir os nomes
#https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/rekognition.html#Rekognition.Client.index_faces
def indexa_colecao(imagens):
    for i in imagens:
        response = client.index_faces(
            CollectionId='faces',
            DetectionAttributes=[],
            ExternalImageId=i[:-4],
            Image={
                'S3Object': {
                    'Bucket': 'fa-images2',
                    'Name': i,
                },
            },
        )

imagens = lista_imagens()
indexa_colecao(imagens)
