function [P, T, TV] = pageblock_data
    %Obtain Random P, T
    
    load train;
    load test;
    


    fid = fopen('train.data','w');
    for i=1:size(train,1)
        fprintf(fid,'%2.8f',train(i,11)-1);
        for j=1:size(train,2)
%            fprintf(fid,' %d:%2.8f',j, P1(i,j));    % for SVM
            fprintf(fid,' %2.8f', (train(i,j)-min(train(:,j)))/(max(train(:,j))-min(train(:,j))));    % for ELM
        end
            fprintf(fid,'\n');
        end
    fclose(fid);

    fid = fopen('test.data','w');    
    for i=1:size(test,1)
        fprintf(fid,'%2.8f',test(i,11)-1);
        for j=1:size(test,2)
%            fprintf(fid,' %d:%2.8f',j, X(i,j));     % for SVM
            fprintf(fid,' %2.8f', (test(i,j)-min(test(:,j)))/(max(test(:,j))-min(test(:,j))));     % for ELM
        end
            fprintf(fid,'\n');
        end
    fclose(fid);