function partition(TrainingData_File,NumberofPart)

%Sample:partition('sat_test',10)
train_data=load(TrainingData_File);
%train_data_s=sort(train_data,1);
lines=size(train_data,1);


%Create The (N+1)the TrainingData_File
fid=fopen(cat(2,TrainingData_File,num2str(NumberofPart+1)),'w+');
j=lines;
while j>=1;
    fprintf(fid,'%f ',train_data(j,:));
    train_data(j,:)=[];
    fprintf(fid,'\n');
    j=j-5;
end
fclose(fid);

interval=fix(size(train_data,1)/NumberofPart);

%Partition the TrainingData_File into N parts
for i=1:NumberofPart
    fid=fopen(cat(2,TrainingData_File,num2str(i)),'w+');
    temp_data=train_data((interval*(i-1)+1):interval*i,:);
    for j=1:interval 
         fprintf(fid,'%f ',temp_data(j,:));
         fprintf(fid,'\n');   
    end
    fclose(fid);
end



clear train_data lines interval temp_data;