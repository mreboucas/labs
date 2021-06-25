import boto3
import json

client = boto3.client('rekognition')
s3 = boto3.resource('s3')
# usa a função da lib rekognition: index_faces
#https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/rekognition.html#Rekognition.Client.index_faces
def detecta_faces():
    faces_detectadas = client.index_faces(
        CollectionId='faces',
        #ALL - option printa um monte de informações
        DetectionAttributes=['DEFAULT'], #essa parte na doc mostra se a pessoa está rindo, idade etc etc, vamos ficar com o DEFAULT
        ExternalImageId='TEMPORARIA',
        Image={
            'S3Object': {
                'Bucket': 'fa-images2',
                'Name': '_analise.jpg',
            },
        }
    )
    return faces_detectadas

def cria_lista_faceId_detecatadas(faces_detactadas):
    faceId_detectadas = []
    for image in range(len(faces_detactadas['FaceRecords'])):
        faceId_detectadas.append(faces_detactadas['FaceRecords'][image]['Face']['FaceId'])
    return faceId_detectadas

# usa a função da lib rekognition: search_faces
# https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/rekognition.html#Rekognition.Client.search_faces
def compara_imagens(faceId_detectadas):
    resultado_comparacao = []
    for ids in faceId_detectadas:
        resultado_comparacao.append(
            client.search_faces(
                CollectionId='faces',
                FaceId=ids,
                FaceMatchThreshold=80,  # % de similaridade da face
                MaxFaces=10, #Qtd de faces a ser retornada
            )
        )
    return resultado_comparacao

def gera_dados_json(resultado_comparacao):
    dados_json=[]
    for face_matches in resultado_comparacao:
        if (len(face_matches.get('FaceMatches'))) >=1:
            perfil = dict(nome=face_matches['FaceMatches'][0]['Face']['ExternalImageId'],
                          faceMatch=round(face_matches['FaceMatches'][0]['Similarity'], 2))
            dados_json.append(perfil)
    return dados_json
#Gera o arquivo dados.json a partir das busca das faces e envia ao bucket: 'fa-site2'
def publica_dados(dados_json):
    arquivo = s3.Object('fa-site2', 'dados.json')
    arquivo.put(Body=json.dumps(dados_json))

#Exclui as imagens temporárias detectadas
def exclui_imagem_colecao(faceId_detectadas):
    print('####' + str(faceId_detectadas))
    client.delete_faces(
        CollectionId='faces',
        FaceIds=faceId_detectadas,
    )

def main (event, context):
    faces_detectadas = detecta_faces()
    #print(json.dumps(faces_detectadas, indent=4))
    faceId_detectadas = cria_lista_faceId_detecatadas(faces_detectadas)
    #print(faceId_detectadas)
    resultado_comparacao = compara_imagens(faceId_detectadas)
    #print('Qtd de faces encontradas: ' + str(len(resultado_comparacao)))
    #print(json.dumps(resultado_comparacao, indent=4))
    dados_json = gera_dados_json(resultado_comparacao)
    publica_dados(dados_json)
    exclui_imagem_colecao(faceId_detectadas)
    print(json.dumps(dados_json, indent=4))

