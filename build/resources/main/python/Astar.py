from PIL import Image
from matplotlib import pyplot as plt
import cv2 as cv

temp = [] #
BluePrint = [] #0 은 통로, 1은 벽
img = cv.imread('/Users/sunminsu/study/temp/aa.png')
Img = img.tolist()
image1 = Image.open('/Users/sunminsu/study/temp/aa.png')
X,Y = image1.size[0], image1.size[1]
for y in range(Y):
    for x in range(X):
        if Img[y][x] == [255,255,255]:
            temp.append(0)
        else:
            temp.append(1)
    BluePrint.append(temp)
    temp = []

class Node:
    def __init__(self, parent=None, position=None):
        self.parent = parent
        self.position = position
        self.g = 0
        self.h = 0
        self.f = 0
    def __eq__(self, other):
        return self.position == other.position

def heuristic(node, goal):  
    dx = abs(node.position[0] - goal.position[0])
    dy = abs(node.position[1] - goal.position[1])
    return dx + dy

def Astar(maze, start, end):
    # startNode와 endNode 초기화
    startNode = Node(None, start)
    endNode = Node(None, end)
    # openList, closedList 초기화
    openList = []
    closedList = []
    # openList에 시작 노드 추가
    openList.append(startNode)

    # endNode를 찾을 때까지 실행
    while openList:

        # 현재 노드 지정
        currentNode = openList[0]
        currentIdx = 0

        # 이미 같은 노드가 openList에 있고, f 값이 더 크면
        # currentNode를 openList안에 있는 값으로 교체
        for index, item in enumerate(openList):
            if item.f < currentNode.f:
                currentNode = item
                currentIdx = index
        # openList에서 제거하고 closedList에 추가
        openList.pop(currentIdx)
        closedList.append(currentNode)

        # 현재 노드가 목적지면 current.position 추가하고
        # current의 부모로 이동
        if currentNode == endNode:
            path = []
            current = currentNode
            while current is not None:
                # maze 길을 표시하려면 주석 해제
                # x, y = current.position
                # maze[x][y] = 7 
                path.append(current.position)
                current = current.parent
            return path[::-1]  # reverse

        children = []
        # 인접한 xy좌표 전부
        for newPosition in [(0, -1), (0, 1), (-1, 0), (1, 0), (-1, -1), (-1, 1), (1, -1), (1, 1)]:

            # 노드 위치 업데이트
            nodePosition = (
                currentNode.position[0] + newPosition[0],  # X
                currentNode.position[1] + newPosition[1])  # Y
                
            # 미로 maze index 범위 안에 있어야함
            within_range_criteria = [
                nodePosition[0] > (len(maze) - 1),
                nodePosition[0] < 0,
                nodePosition[1] > (len(maze[len(maze) - 1]) - 1),
                nodePosition[1] < 0,
            ]

            if any(within_range_criteria):  # 하나라도 true면 범위 밖임
                continue

            # 장애물이 있으면 다른 위치 불러오기
            if maze[nodePosition[0]][nodePosition[1]] != 0:
                continue

            new_node = Node(currentNode, nodePosition)
            children.append(new_node)

        # 자식들 모두 loop
        for child in children:
            # 자식이 closedList에 있으면 continue
            if child in closedList:
                continue

            # f, g, h값 업데이트
            child.g = currentNode.g + 1
            child.h = heuristic(child, endNode) #다른 휴리스틱
            #print("position:", child.position) #거리 추정 값 보기
            child.f = child.g + child.h

            # 자식이 openList에 있으고, g값이 더 크면 continue
            if len([openNode for openNode in openList
                    if child == openNode and child.g > openNode.g]) > 0:
                continue

            openList.append(child)
        

def main():
    Rpath = []
    #cal row
    maze = BluePrint
    start = (230, 154)
    end = (36, 110)
    
  
    #Exit(26,83)
    #exit(276,20)
    path = Astar(maze, start, end)
    for i in path:
        Rpath.append((i[1],i[0]))
    
    x0,y0=path[0]
    for vertex in path[1:]:
        x1,y1=vertex
        cv.line(img,(y0,x0),(y1,x1),(255,0,0),2)
        x0,y0=vertex
    #print(Rpath)
    plt.figure()
    plt.imshow(img)
    plt.show()




if __name__ == '__main__':
    main()
