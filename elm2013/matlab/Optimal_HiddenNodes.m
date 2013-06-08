function [Optimal_TrainingAccuracy,Optimal_TestingAccuracy,Optimal_NumberofHiddenNeurons]=Optimal_HiddenNodes(TrainingData_File,TestingData_File,Elm_Type,ActivationFunction)
%Sample1:[Optimal_TrainingAccuracy,Optimal_TestingAccuracy,Optimal_NumberofHiddenNeurons]=Optimal_HiddenNodes('diabetes_train','diabetes_test',1,'sig')

%%%%%%%%%%%% Macro definition
MAX=200;

%initialization
X=zeros(1,MAX);
Train_Y=zeros(1,MAX);
Test_Y=zeros(1,MAX);

for i=1:MAX
    NumberofHiddenNeurons=i;
    X(i)=NumberofHiddenNeurons;
    [TrainingTime,TestingTime,TrainingAccuracy,TestingAccuracy]=elm(TrainingData_File,TestingData_File,Elm_Type,NumberofHiddenNeurons,ActivationFunction);  
    Train_Y(i)=TrainingAccuracy;
    Test_Y(i)=TestingAccuracy;
end
%drawing
plot(X,Train_Y,X,Test_Y)
xlabel('NumberofHiddenNeurons');
ylabel('Accuracy');
title('Optimal HiddenNode');
    
[Optimal_TestingAccuracy,index]=max(Test_Y);
Optimal_NumberofHiddenNeurons=index;
[TrainingTime,TestingTime,Optimal_TrainingAccuracy,Optimal_TestingAccuracy]=elm(TrainingData_File,TestingData_File,Elm_Type,Optimal_NumberofHiddenNeurons,ActivationFunction);

end